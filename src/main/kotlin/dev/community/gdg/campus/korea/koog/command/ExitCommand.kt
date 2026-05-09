package dev.community.gdg.campus.korea.koog.command

class ExitCommand : Command {
    override val name = "exit"
    override val aliases = listOf("quit", "q")
    override val description = "프로그램을 종료합니다"

    override suspend fun execute(args: List<String>) = CommandResult.Exit
}
