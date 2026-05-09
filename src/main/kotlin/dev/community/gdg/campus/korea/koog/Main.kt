package dev.community.gdg.campus.korea.koog

import ai.koog.agents.chatMemory.feature.ChatMemory
import ai.koog.agents.chatMemory.feature.InMemoryChatHistoryProvider
import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.agents.features.eventHandler.feature.EventHandlerConfig
import ai.koog.agents.features.eventHandler.feature.handleEvents
import ai.koog.prompt.executor.clients.LLMClientException
import ai.koog.prompt.executor.clients.google.GoogleModels
import ai.koog.prompt.executor.llms.all.simpleGoogleAIExecutor
import dev.community.gdg.campus.korea.koog.command.ClearCommand
import dev.community.gdg.campus.korea.koog.command.CommandRegistry
import dev.community.gdg.campus.korea.koog.command.CommandResult
import dev.community.gdg.campus.korea.koog.command.ExitCommand
import dev.community.gdg.campus.korea.koog.command.HelpCommand
import dev.community.gdg.campus.korea.koog.tools.generateExamPrep
import dev.community.gdg.campus.korea.koog.tools.listFiles
import dev.community.gdg.campus.korea.koog.tools.readFile
import dev.community.gdg.campus.korea.koog.tools.saveNote
import dev.community.gdg.campus.korea.koog.ui.Banner
import kotlinx.coroutines.runBlocking
import java.util.UUID

/** Koog `agent.run` 두 번째 인자. `/clear`마다 갱신해 대화 메모리·세션 추적이 이전과 섞이지 않게 한다. */
private fun newStudyChatSessionId(): String = "study-session-${UUID.randomUUID()}"

val studyBuddyPrompt = """
    너는 컴퓨터공학과 학생의 과제와 시험 준비를 도와주는 조교야.

    규칙:
    - 핵심만 간결하게 설명해. 대학생은 바쁘니까.
    - 과제에 바로 쓸 수 있는 실용적인 답변을 해.
    - 코드 관련 질문에는 C++로 예시를 들어줘.
    - 개념 설명은 "한 줄 요약 → 상세 설명" 순서로 해.
    - 반드시 도구를 사용해서 파일을 읽고 저장해.
    - 항상 한국어로 답변해.
""".trimIndent()

private fun EventHandlerConfig.echoToolCallsToStdout() {
    onToolCallStarting { println("  🔧 [도구 호출] ${it.toolName}...") }
    onToolCallCompleted { println("  ✅ [도구 완료] ${it.toolName}") }
}

private suspend fun runStudyStage(
    label: String,
    stageLabel: String,
    sessionId: String,
    block: suspend () -> String,
): String {
    try {
        return block()
    } catch (e: LLMClientException) {
        val msg = e.message.orEmpty()
        val isQuota =
            msg.contains("429") ||
                msg.contains("RESOURCE_EXHAUSTED") ||
                msg.contains("quota", ignoreCase = true)
        if (isQuota) {
            System.err.println(
                """

                [$label 할당량 초과] Gemini API 요청 한도에 걸렸습니다. (무료 등급은 모델·프로젝트별 일일 요청 수 제한이 있습니다.)
                단계: $stageLabel (세션: $sessionId)
                몇 분 후 재시도하거나, Google AI Studio에서 한도·청구를 확인하세요.
                문서: https://ai.google.dev/gemini-api/docs/rate-limits

                """.trimIndent(),
            )
        } else {
            System.err.println("\n[$label 오류] $stageLabel — ${e.message}\n")
        }
        throw e
    }
}

// 순차 오케스트레이션 — 역할별 System Prompt와 도구 조합으로 전문가 에이전트 세 명 실행 (파일 시스템 = 공유 메모리)
suspend fun runStudyTeam(apiKey: String) {
    val reviewerAgent = AIAgent(
        promptExecutor = simpleGoogleAIExecutor(apiKey),
        systemPrompt = """
            너는 강의자료 복습 전문가야.
            강의 노트를 읽고 핵심 개념만 추출해서 간결한 복습 노트를 만들어.
            키워드 중심, 시험에 나올 내용 위주로 정리해.
            반드시 도구를 사용해서 파일을 읽고 저장해.
            항상 한국어로 답변해.
        """.trimIndent(),
        llmModel = GoogleModels.Gemini2_5Flash,
        toolRegistry = ToolRegistry {
            tool(::readFile)
            tool(::saveNote)
        },
    ) {
        handleEvents {
            echoToolCallsToStdout()
        }
    }

    val assignmentAgent = AIAgent(
        promptExecutor = simpleGoogleAIExecutor(apiKey),
        systemPrompt = """
            너는 과제 분석 전문가야.
            과제 요구사항을 분석하고, 기존 복습 노트를 참고해서 풀이 전략을 세워.
            학생 코드가 있으면 버그도 찾아줘.
            반드시 도구를 사용해서 파일을 읽고 저장해.
            항상 한국어로 답변해.
        """.trimIndent(),
        llmModel = GoogleModels.Gemini2_5Flash,
        toolRegistry = ToolRegistry {
            tool(::readFile)
            tool(::listFiles)
            tool(::saveNote)
        },
    ) {
        handleEvents {
            echoToolCallsToStdout()
        }
    }

    val examPrepAgent = AIAgent(
        promptExecutor = simpleGoogleAIExecutor(apiKey),
        systemPrompt = """
            너는 시험 대비 자료 전문가야.
            축적된 복습 노트를 종합해서 치트시트와 예상문제를 만들어.
            치트시트는 A4 1장 이내, 예상문제는 과제 패턴 기반으로 3문제.
            반드시 도구를 사용해서 파일을 읽고 저장해.
            항상 한국어로 답변해.
        """.trimIndent(),
        llmModel = GoogleModels.Gemini2_5Flash,
        toolRegistry = ToolRegistry {
            tool(::readFile)
            tool(::listFiles)
            tool(::generateExamPrep)
        },
    ) {
        handleEvents {
            echoToolCallsToStdout()
        }
    }

    println("=== 학습 전문가 팀 가동! ===\n")

    println("[1/3] 복습 정리가가 강의자료를 분석합니다...")
    val reviewResult = runStudyStage("전문가 팀", "복습 정리", "study-team-review") {
        reviewerAgent.run(
            "lecture-notes/week08-avl-tree.md 강의자료를 읽고 복습 노트를 만들어줘",
            "study-team-review",
        )
    }
    println("복습 완료: $reviewResult\n")

    println("[2/3] 과제 도우미가 과제를 분석합니다...")
    val assignmentResult = runStudyStage("전문가 팀", "과제 분석", "study-team-assignment") {
        assignmentAgent.run(
            """
                assignments/hw-avl-tree.md 과제를 분석하고, notes/ 폴더의 복습 노트를 참고해서 풀이 가이드를 만들어줘.
                student-code/avl_tree.cpp도 확인해줘.
            """.trimIndent(),
            "study-team-assignment",
        )
    }
    println("과제 분석 완료: $assignmentResult\n")

    println("[3/3] 시험 대비가가 자료를 생성합니다...")
    val examPrepResult = runStudyStage("전문가 팀", "시험 대비", "study-team-exam") {
        examPrepAgent.run(
            "notes/ 폴더의 모든 노트를 읽고 치트시트와 예상문제를 만들어줘",
            "study-team-exam",
        )
    }
    println("시험 대비 자료 완료: $examPrepResult")

    println("\n=== 전문가 팀 작업 완료! ===")
}

private fun createAgent(apiKey: String) =
    AIAgent(
        promptExecutor = simpleGoogleAIExecutor(apiKey),
        systemPrompt = studyBuddyPrompt,
        llmModel = GoogleModels.Gemini2_5Flash,
        toolRegistry =
            ToolRegistry {
                tool(::readFile)
                tool(::saveNote)
                tool(::listFiles)
                tool(::generateExamPrep)
            },
    ) {
        install(ChatMemory) {
            chatHistoryProvider = InMemoryChatHistoryProvider()
            windowSize(20)
        }
        handleEvents {
            echoToolCallsToStdout()
        }
    }

/** [runStudySession]과 동일한 분기: 슬래시면 레지스트리, 아니면 에이전트. 입력은 trim 되었고 비어 있지 않음. */
sealed class CliInputRoute {
    data class Command(val result: CommandResult) : CliInputRoute()

    data object UnknownSlashCommand : CliInputRoute()

    data class Agent(val message: String) : CliInputRoute()
}

suspend fun routeCliInput(trimmedNonBlankInput: String, registry: CommandRegistry): CliInputRoute {
    require(trimmedNonBlankInput.isNotBlank()) { "trimmed non-blank input expected" }
    if (!trimmedNonBlankInput.startsWith("/")) {
        return CliInputRoute.Agent(trimmedNonBlankInput)
    }
    val result = registry.execute(trimmedNonBlankInput)
    return if (result == null) CliInputRoute.UnknownSlashCommand else CliInputRoute.Command(result)
}

// CLI 대화형 세션 (Banner + Command 패턴 적용)
suspend fun runStudySession(apiKey: String) {
    val commandRegistry = CommandRegistry()
    commandRegistry.registerAll(
        HelpCommand(commandRegistry),
        ExitCommand(),
        ClearCommand(),
    )

    Banner.printWelcome()
    var agent = createAgent(apiKey)
    var chatSessionId = newStudyChatSessionId()

    while (true) {
        print("학생 > ")
        val input = readLine()?.trim()
        if (input == null) {
            Banner.printGoodbye()
            break
        }
        if (input.isBlank()) continue

        when (val route = routeCliInput(input, commandRegistry)) {
            is CliInputRoute.Command ->
                when (val result = route.result) {
                    CommandResult.Exit -> {
                        Banner.printGoodbye()
                        return
                    }
                    CommandResult.ClearSession -> {
                        chatSessionId = newStudyChatSessionId()
                        agent = createAgent(apiKey)
                        continue
                    }
                    is CommandResult.Success -> continue
                    is CommandResult.Error -> {
                        println("  ❌ ${result.message}")
                        continue
                    }
                }
            CliInputRoute.UnknownSlashCommand -> {
                println("  알 수 없는 명령어입니다. /help를 입력해보세요.")
                continue
            }
            is CliInputRoute.Agent -> {
                try {
                    val response = agent.run(route.message, chatSessionId)
                    println("\n조교 > $response\n")
                } catch (e: LLMClientException) {
                    val msg = e.message.orEmpty()
                    val isQuota =
                        msg.contains("429") ||
                            msg.contains("RESOURCE_EXHAUSTED") ||
                            msg.contains("quota", ignoreCase = true)
                    if (isQuota) {
                        System.err.println(
                            """

                            [할당량 초과] Gemini API 요청 한도에 걸렸습니다. (무료 등급은 모델·프로젝트별 일일 요청 수 제한이 있습니다.)
                            몇 분 후 재시도하거나, Google AI Studio에서 한도·청구를 확인하세요.
                            문서: https://ai.google.dev/gemini-api/docs/rate-limits

                            """.trimIndent(),
                        )
                    } else {
                        System.err.println("\n[조교 호출 오류] ${e.message}\n")
                    }
                }
            }
        }
    }
}

/** 실행 모드: 기본값은 비대화형 팀 배치([runStudyTeam]). 대화형은 `--repl` 또는 `KOOG_MODE=repl`. */
internal enum class StudyRunMode {
    Team,
    Repl,
}

/** `--repl` / `--team`은 환경변수보다 우선한다. [koogModeEnv]는 테스트용 오버라이드; 기본은 `KOOG_MODE` 환경변수. */
internal fun resolveStudyRunMode(
    args: Array<String>,
    koogModeEnv: String? = System.getenv("KOOG_MODE"),
): StudyRunMode {
    val hasRepl = args.any { it == "--repl" }
    val hasTeam = args.any { it == "--team" }
    require(!(hasRepl && hasTeam)) { "--repl 과 --team 을 함께 쓸 수 없습니다." }
    if (hasRepl) return StudyRunMode.Repl
    if (hasTeam) return StudyRunMode.Team

    return when (koogModeEnv?.trim()?.lowercase()) {
        null, "", "team", "batch", "pipeline" -> StudyRunMode.Team
        "repl", "session", "interactive" -> StudyRunMode.Repl
        else ->
            error(
                "KOOG_MODE 는 team(repl 전 외 기본) 또는 repl 입니다. " +
                    "허용 예: team, batch, pipeline, repl, session, interactive",
            )
    }
}

fun main(args: Array<String>) =
    runBlocking {
        val apiKey =
            System.getenv("GOOGLE_API_KEY")
                ?: error("GOOGLE_API_KEY 환경변수를 설정해주세요!")
        when (resolveStudyRunMode(args)) {
            StudyRunMode.Team -> runStudyTeam(apiKey)
            StudyRunMode.Repl -> runStudySession(apiKey)
        }
    }
