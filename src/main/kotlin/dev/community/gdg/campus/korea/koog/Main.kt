package dev.community.gdg.campus.korea.koog

import ai.koog.agents.chatMemory.feature.ChatMemory
import ai.koog.agents.chatMemory.feature.InMemoryChatHistoryProvider
import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.prompt.executor.clients.google.GoogleModels
import ai.koog.prompt.executor.llms.all.simpleGoogleAIExecutor
import dev.community.gdg.campus.korea.koog.tools.generateExamPrep
import dev.community.gdg.campus.korea.koog.tools.listFiles
import dev.community.gdg.campus.korea.koog.tools.readFile
import dev.community.gdg.campus.korea.koog.tools.saveNote
import kotlinx.coroutines.runBlocking

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

// 대화형 과제 도움 세션
suspend fun runStudySession(apiKey: String) {
    val toolRegistry = ToolRegistry {
        tool(::readFile)
        tool(::saveNote)
        tool(::generateExamPrep)
        tool(::listFiles)
    }

    val agent = AIAgent(
        promptExecutor = simpleGoogleAIExecutor(apiKey),
        systemPrompt = studyBuddyPrompt,
        llmModel = GoogleModels.Gemini2_5Flash,
        toolRegistry = toolRegistry,
    ) {
        install(ChatMemory) {
            chatHistoryProvider = InMemoryChatHistoryProvider()
            windowSize(20)
        }
    }

    println("=== 과제 도우미 시작 ===")
    println("질문을 입력하세요 (종료: exit)\n")

    while (true) {
        print("학생 > ")
        val input = readLine() ?: break
        if (input == "exit") break

        val response = agent.run(input, "study-session")
        println("\n조교 > $response\n")
    }
}

fun main() = runBlocking {
    val apiKey = System.getenv("GOOGLE_API_KEY")
        ?: error("GOOGLE_API_KEY 환경변수를 설정해주세요!")
    runStudySession(apiKey)
}
