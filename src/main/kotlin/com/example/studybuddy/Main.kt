package com.example.studybuddy

import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val apiKey = System.getenv("GOOGLE_API_KEY")
        ?: error("GOOGLE_API_KEY 환경변수를 설정해주세요!")

    // TODO: 코드랩을 진행하면서 여기에 에이전트 코드를 작성합니다
    println("Study Buddy Agent 프로젝트가 준비되었습니다!")
    println("API Key: ${apiKey.take(8)}...")
}
