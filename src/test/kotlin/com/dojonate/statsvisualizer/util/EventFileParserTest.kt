package com.dojonate.statsvisualizer.util

import com.dojonate.statsvisualizer.model.EventType
import com.dojonate.statsvisualizer.model.Team
import com.dojonate.statsvisualizer.service.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary

@SpringBootTest
@Import(EventFileParserTest.TestConfig::class)
class EventFileParserTest {

    @Autowired
    private lateinit var eventFileParser: EventFileParser

    @Autowired
    private lateinit var teamService: TeamService

    private lateinit var validEventFileContent: String
    private lateinit var invalidEventFileContent: String

    @BeforeEach
    fun setUp() {
        whenever(teamService.findOrCreateTeam(eq("HOU"), anyString(), anyString()))
            .thenReturn(Team("HOU", "Houston Astros", "American League", 1962, 2024))
        whenever(teamService.findOrCreateTeam(eq("TEX"), anyString(), anyString()))
            .thenReturn(Team("TEX", "Texas Rangers", "American League", 1961, 2024))

        validEventFileContent = """
            id,HOU20230615
            info,visteam,TEX
            info,hometeam,HOU
            play,1,0,choos001,10,BX,S8.2-H;1-3
            play,1,1,valbl001,32,BFBFX,D8/F.3-H;B-2
        """.trimIndent()

        invalidEventFileContent = """
            id,HOUXXXXXXX
            play,1,0,choos001,10,BX,S8.2H;1-3
        """.trimIndent() // Invalid ID and malformed runner advances
    }

    @Test
    fun `should parse valid event file`() {
        val game = eventFileParser.parse(validEventFileContent)

        // Assertions for basic game properties
        assertEquals("HOU20230615", game.id)
        assertEquals("TEX", game.awayTeam.teamId)
        assertEquals("HOU", game.homeTeam.teamId)
        assertEquals(2, game.playerEvents.size)

        // Assertions for specific PlayerEvents
        val firstEvent = game.playerEvents[0]
        assertEquals(EventType.SINGLE, firstEvent.eventType[0])
        assertEquals("2-H", firstEvent.runnerAdvances[0].baseMovement)
        assertEquals("1-3", firstEvent.runnerAdvances[1].baseMovement)

        val secondEvent = game.playerEvents[1]
        assertEquals(EventType.DOUBLE, secondEvent.eventType[0])
        assertEquals("3-H", secondEvent.runnerAdvances[0].baseMovement)
        assertEquals("B-2", secondEvent.runnerAdvances[1].baseMovement)
    }

    @Test
    fun `should fail parsing invalid event file`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            eventFileParser.parse(invalidEventFileContent.trimIndent())
        }

        assertEquals("Invalid event file.", exception.message)
    }

    @Test
    fun `should parse complex runner advances`() {
        val complexEventContent = """
            id,HOU20230616
            play,4,1,gonzm002,32,CBFBFX,S9/L-.2-H;B-2(TH)
        """.trimIndent()

        val game = eventFileParser.parse(complexEventContent)
        val playerEvent = game.playerEvents.first()

        // Validate event details
        assertEquals(EventType.SINGLE, playerEvent.eventType[0])

        // Validate runner advances
        val runnerAdvances = playerEvent.runnerAdvances
        assertEquals(2, runnerAdvances.size)

        val firstAdvance = runnerAdvances[0]
        assertEquals("2-H", firstAdvance.baseMovement)
        assertNull(firstAdvance.details) // No details for this advance

        val secondAdvance = runnerAdvances[1]
        assertEquals("B-2", secondAdvance.baseMovement)
        assertEquals("TH", secondAdvance.details) // Throw to second
    }

    @Test
    fun `should parse event with modifiers`() {
        val modifierEventContent = """
            id,HOU20230617
            play,5,0,choos001,12,CSBBX,64(1)3/GDP/G6
        """.trimIndent()

        val game = eventFileParser.parse(modifierEventContent)
        val playerEvent = game.playerEvents.first()

        // Validate event details
        assertEquals(EventType.DOUBLE_PLAY, playerEvent.eventType[1])
        assertEquals(EventType.GROUND_BALL, playerEvent.eventType[2])
    }

    @Test
    fun `should handle malformed runner advances gracefully`() {
        val malformedRunnerAdvanceContent = """
        id,HOU20230618
        play,5,1,gonzm002,12,CSBBX,S9/L-.2H;B2(TH)
        """.trimIndent()

        val exception = assertThrows(IllegalArgumentException::class.java) {
            eventFileParser.parse(malformedRunnerAdvanceContent)
        }

        assertEquals("Invalid runner advance format.", exception.message)
    }

    @TestConfiguration
    open class TestConfig {
        @Bean
        @Primary
        open fun mockTeamService(): TeamService = mock(TeamService::class.java)

        @Bean
        @Primary
        open fun playerService(): PlayerService = mock(PlayerService::class.java)

        @Bean
        open fun eventFileParser(): EventFileParser =
            EventFileParser(
                mockTeamService(),
                mock(PlayerService::class.java),
                mock(GameService::class.java),
                mock(PlayerEventService::class.java),
                mock(RosterEntryService::class.java)
            )
    }
}
