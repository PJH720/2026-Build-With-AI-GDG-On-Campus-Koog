package dev.community.gdg.campus.korea.koog.command

class HelpCommand(private val registry: CommandRegistry) : Command {
    override val name = "help"
    override val aliases = listOf("h", "?")
    override val description = "사용 가능한 명령어를 보여줍니다"

    override suspend fun execute(args: List<String>): CommandResult {
        println("\n📋 사용 가능한 명령어:\n")
        registry.getAllCommands().sortedBy { it.name }.forEach { cmd ->
            val aliasText =
                if (cmd.aliases.isNotEmpty()) {
                    " (${cmd.aliases.joinToString(", ") { "/$it" }})"
                } else {
                    ""
                }
            println("  ${cmd.usage}$aliasText")
            println("    ${cmd.description}")
        }
        println()
        return CommandResult.Success()
    }
}
