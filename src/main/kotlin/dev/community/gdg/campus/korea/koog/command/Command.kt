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

class CommandRegistry {
    private val commands = mutableListOf<Command>()

    fun register(command: Command) {
        commands.add(command)
    }

    fun registerAll(vararg cmds: Command) {
        commands.addAll(cmds)
    }

    fun getAllCommands(): List<Command> = commands.toList()

    suspend fun execute(input: String): CommandResult? {
        val trimmed = input.trim()
        val command = commands.find { it.matches(trimmed) } ?: return null
        val args =
            trimmed.removePrefix("/").split(" ").drop(1).filter { it.isNotBlank() }
        return command.execute(args)
    }
}
