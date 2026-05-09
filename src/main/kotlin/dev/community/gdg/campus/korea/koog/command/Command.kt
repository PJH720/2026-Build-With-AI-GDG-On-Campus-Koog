package dev.community.gdg.campus.korea.koog.command

interface Command {
    val name: String
    val aliases: List<String> get() = emptyList()
    val description: String
    val usage: String get() = "/$name"
    suspend fun execute(args: List<String> = emptyList()): CommandResult

    fun matches(input: String): Boolean {
        val trimmed = input.trim()
        if (!trimmed.startsWith("/")) return false
        val commandName =
            trimmed.removePrefix("/")
                .split(" ")
                .firstOrNull()
                ?.takeIf { it.isNotBlank() }
                ?: return false
        val key = commandName.lowercase()
        return key == name.lowercase() || aliases.any { it.lowercase() == key }
    }
}

sealed class CommandResult {
    data class Success(val message: String = "") : CommandResult()

    data class Error(val message: String) : CommandResult()

    data object Exit : CommandResult()

    data object ClearSession : CommandResult()
}

/**
 * `execute`의 반환 타입 `CommandResult?`에서 **`null`은 ‘등록된 명령 중 입력과 매칭되는 것이 하나도 없을 때’만** 의미한다.
 * 슬래시 뒤 첫 토큰이 비어 있거나 공백만 있는 경우, 알려지지 않은 명령 이름인 경우 등은 `Command.matches` 기준으로 모두 매칭 실패로 처리되며,
 * 별도의 `CommandResult`로 ‘파싱 실패’와 ‘미등록 명령’을 구분하지 않는다. 명령별 인자 오류는 각 `Command.execute`가 `CommandResult.Error`로 표현한다.
 */
class CommandRegistry {
    private val commands = mutableListOf<Command>()

    fun register(command: Command) {
        commands.add(command)
    }

    fun registerAll(vararg cmds: Command) {
        commands.addAll(cmds)
    }

    fun getAllCommands(): List<Command> = commands.toList()

    /** 매칭되는 명령이 없으면 `null` — 의미는 이 클래스 블록 KDoc 참고. */
    suspend fun execute(input: String): CommandResult? {
        val trimmed = input.trim()
        val command = commands.find { it.matches(trimmed) } ?: return null
        val args =
            trimmed.removePrefix("/").split(" ").drop(1).filter { it.isNotBlank() }
        return command.execute(args)
    }
}
