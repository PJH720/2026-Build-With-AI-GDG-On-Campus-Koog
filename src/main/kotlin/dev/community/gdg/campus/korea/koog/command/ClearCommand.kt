package dev.community.gdg.campus.korea.koog.command

class ClearCommand : Command {
    override val name = "clear"
    override val aliases = listOf("reset")
    override val description =
        "대화 기록을 비우고 새 세션 ID로 이어갑니다 (에이전트·메모리 재생성)"

    override suspend fun execute(args: List<String>): CommandResult {
        println("🔄 대화를 초기화했습니다. 새 세션으로 계속합니다.")
        return CommandResult.ClearSession
    }
}
