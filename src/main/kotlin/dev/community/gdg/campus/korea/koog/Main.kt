package dev.community.gdg.campus.korea.koog

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.prompt.executor.clients.google.GoogleModels
import ai.koog.prompt.executor.llms.all.simpleGoogleAIExecutor
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

val toolRegistry = ToolRegistry {
    tool(::readFile)
    tool(::saveNote)
    tool(::listFiles)
}

fun main() = runBlocking {
    val apiKey = System.getenv("GOOGLE_API_KEY")
        ?: error("GOOGLE_API_KEY 환경변수를 설정해주세요!")

    val agent = AIAgent(
        promptExecutor = simpleGoogleAIExecutor(apiKey),
        systemPrompt = studyBuddyPrompt,
        llmModel = GoogleModels.Gemini2_5Flash,
        toolRegistry = toolRegistry
    )

    val response = agent.run(
        """
        1. assignments/hw-avl-tree.md 과제 요구사항을 읽어줘
        2. notes/ 폴더에 있는 기존 복습 노트도 참고해줘
        3. student-code/avl_tree.cpp 학생 코드를 분석해줘
        4. 과제 풀이 가이드를 만들어서 notes/hw-avl-guide.md로 저장해줘
        """.trimIndent()
    )
    println(response)
}
