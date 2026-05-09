package dev.community.gdg.campus.korea.koog

import dev.community.gdg.campus.korea.koog.command.ClearCommand
import dev.community.gdg.campus.korea.koog.command.CommandRegistry
import dev.community.gdg.campus.korea.koog.command.CommandResult
import dev.community.gdg.campus.korea.koog.command.ExitCommand
import dev.community.gdg.campus.korea.koog.command.HelpCommand
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * [routeCliInput]은 [runStudySession]과 동일 분기다.
 *
 * 다음 입력별 경로를 검증한다.
 *
 * | 입력 | 기대 경로 |
 * |------|------------|
 * | `/help` | [CliInputRoute.Command] → [CommandResult.Success] |
 * | `/exit` | [CliInputRoute.Command] → [CommandResult.Exit] |
 * | `/clear` | [CliInputRoute.Command] → [CommandResult.ClearSession] |
 * | 존재하지 않는 `/foo` | [CliInputRoute.UnknownSlashCommand] ([agent.run] 아님) |
 * | 슬래시 없는 일반 문장 | [CliInputRoute.Agent] ([CommandResult] 아님) |
 */
class CliInputRoutingTest {

    private fun registryLikeMain(): CommandRegistry =
        CommandRegistry().apply {
            registerAll(
                HelpCommand(this),
                ExitCommand(),
                ClearCommand(),
            )
        }

    @Test
    fun slashHelp_goesTo_commandResult_success_notAgent() =
        runBlocking {
            val route = routeCliInput("/help", registryLikeMain())
            assertIs<CliInputRoute.Command>(route)
            assertTrue(route.result is CommandResult.Success)
            assertFalse(route is CliInputRoute.Agent)
            assertFalse(route is CliInputRoute.UnknownSlashCommand)
        }

    @Test
    fun slashExit_goesTo_commandResult_exit_notAgent() =
        runBlocking {
            val route = routeCliInput("/exit", registryLikeMain())
            assertIs<CliInputRoute.Command>(route)
            assertEquals(CommandResult.Exit, route.result)
            assertFalse(route is CliInputRoute.Agent)
            assertFalse(route is CliInputRoute.UnknownSlashCommand)
        }

    @Test
    fun slashClear_goesTo_commandResult_clearSession_notAgent() =
        runBlocking {
            val route = routeCliInput("/clear", registryLikeMain())
            assertIs<CliInputRoute.Command>(route)
            assertEquals(CommandResult.ClearSession, route.result)
            assertFalse(route is CliInputRoute.Agent)
            assertFalse(route is CliInputRoute.UnknownSlashCommand)
        }

    @Test
    fun unknownSlash_goesTo_unknownSlashCommand_notCommandResult_notAgent() =
        runBlocking {
            val route = routeCliInput("/foo", registryLikeMain())
            assertIs<CliInputRoute.UnknownSlashCommand>(route)
            assertFalse(route is CliInputRoute.Command)
            assertFalse(route is CliInputRoute.Agent)
        }

    @Test
    fun plainSentence_goesTo_agentPath_notCommandResult_notUnknownSlash() =
        runBlocking {
            val msg = "오늘 학습 계획 짜줘"
            val route = routeCliInput(msg, registryLikeMain())
            assertIs<CliInputRoute.Agent>(route)
            assertEquals(msg, route.message)
            assertFalse(route is CliInputRoute.Command)
            assertFalse(route is CliInputRoute.UnknownSlashCommand)
        }
}

class ResolveStudyRunModeTest {

    @Test
    fun default_noFlag_emptyEnv_isRepl() {
        assertEquals(StudyRunMode.Repl, resolveStudyRunMode(emptyArray(), koogModeEnv = null))
        assertEquals(StudyRunMode.Repl, resolveStudyRunMode(emptyArray(), koogModeEnv = ""))
        assertEquals(StudyRunMode.Repl, resolveStudyRunMode(emptyArray(), koogModeEnv = "  "))
    }

    @Test
    fun envRepl_aliases_resolveToRepl() {
        assertEquals(StudyRunMode.Repl, resolveStudyRunMode(emptyArray(), koogModeEnv = "repl"))
        assertEquals(StudyRunMode.Repl, resolveStudyRunMode(emptyArray(), koogModeEnv = "SESSION"))
        assertEquals(StudyRunMode.Repl, resolveStudyRunMode(emptyArray(), koogModeEnv = "Interactive"))
    }

    @Test
    fun envTeam_aliases_resolveToTeam() {
        assertEquals(StudyRunMode.Team, resolveStudyRunMode(emptyArray(), koogModeEnv = "team"))
        assertEquals(StudyRunMode.Team, resolveStudyRunMode(emptyArray(), koogModeEnv = "BATCH"))
        assertEquals(StudyRunMode.Team, resolveStudyRunMode(emptyArray(), koogModeEnv = "pipeline"))
    }

    @Test
    fun cliRepl_overridesEnvRepl() {
        assertEquals(
            StudyRunMode.Repl,
            resolveStudyRunMode(arrayOf("--repl"), koogModeEnv = "repl"),
        )
    }

    @Test
    fun cliRepl_overridesEnvTeam() {
        assertEquals(
            StudyRunMode.Repl,
            resolveStudyRunMode(arrayOf("--repl"), koogModeEnv = "team"),
        )
    }

    @Test
    fun cliTeam_overridesEnvRepl() {
        assertEquals(
            StudyRunMode.Team,
            resolveStudyRunMode(arrayOf("--team"), koogModeEnv = "repl"),
        )
    }

    @Test
    fun bothReplAndTeam_throws() {
        assertFailsWith<IllegalArgumentException> {
            resolveStudyRunMode(arrayOf("--repl", "--team"), koogModeEnv = null)
        }
    }

    @Test
    fun unknownEnv_throws() {
        assertFailsWith<IllegalStateException> {
            resolveStudyRunMode(emptyArray(), koogModeEnv = "not-a-mode")
        }
    }
}

