plugins {
    kotlin("jvm") version "2.3.21"
    kotlin("plugin.serialization") version "2.3.21"
    application
}

group = "dev.community.gdg.campus.korea.koog"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("ai.koog:koog-agents:0.8.0")
    implementation("ai.koog:agents-features-memory:0.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
    implementation("ch.qos.logback:logback-classic:1.5.18")
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("dev.community.gdg.campus.korea.koog.MainKt")
}

// Gradle run은 기본으로 stdin을 JVM에 넘기지 않아 readLine()이 바로 null이 됨 → 대화형 입력 위해 연결
tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}
