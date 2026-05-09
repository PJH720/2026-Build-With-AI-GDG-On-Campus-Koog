package dev.community.gdg.campus.korea.koog.command

class ClearCommand : Command {
    override val name = "clear"
    override val aliases = listOf("reset")
    override val description = "대화를 초기화하고 새로 시작합니다"

    override suspend fun execute(args: List<String>): CommandResult {
        println("🔄 세션이 초기화되었습니다. 새로 시작합니다!")
        return CommandResult.ClearSession
    }
}
