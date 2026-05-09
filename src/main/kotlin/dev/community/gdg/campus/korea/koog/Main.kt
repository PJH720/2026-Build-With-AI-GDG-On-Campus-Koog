package dev.community.gdg.campus.korea.koog

import ai.koog.agents.core.agent.AIAgent
import ai.koog.prompt.executor.llms.all.simpleGoogleAIExecutor
import ai.koog.prompt.executor.clients.google.GoogleModels
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val apiKey = System.getenv("GOOGLE_API_KEY")
        ?: error("GOOGLE_API_KEY 환경변수를 설정해주세요!")

    val agent = AIAgent(
        promptExecutor = simpleGoogleAIExecutor(apiKey),
        systemPrompt = """
            너는 컴퓨터공학과 학생의 과제와 시험 준비를 도와주는 조교야.

            규칙:
            - 핵심만 간결하게 설명해. 대학생은 바쁘니까.
            - 과제에 바로 쓸 수 있는 실용적인 답변을 해.
            - 코드 관련 질문에는 C++로 예시를 들어줘.
            - 개념 설명은 "한 줄 요약 → 상세 설명" 순서로 해.
        """.trimIndent(),
        llmModel = GoogleModels.Gemini2_5Flash
    )

    val response = agent.run("AVL Tree가 뭐야? BST랑 뭐가 달라?")
    println(response)
}
