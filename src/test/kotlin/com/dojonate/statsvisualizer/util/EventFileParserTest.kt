package com.dojonate.statsvisualizer.util

import com.dojonate.statsvisualizer.model.EventType
import com.dojonate.statsvisualizer.service.TeamService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EventFileParserTest {

    @Autowired
    private lateinit var eventFileParser: EventFileParser

    @Autowired
    private lateinit var teamService: TeamService

    private lateinit var validEventFileContent: String
    private lateinit var invalidEventFileContent: String

    @BeforeEach
    fun setUp() {
//        whenever(teamService.findOrCreateTeam(eq("HOU"), anyString(), anyString()))
//            .thenReturn(Team("HOU", "Houston Astros", "American League", 1962, 2024))
//        whenever(teamService.findOrCreateTeam(eq("TEX"), anyString(), anyString()))
//            .thenReturn(Team("TEX", "Texas Rangers", "American League", 1961, 2024))

        validEventFileContent = """
            id,HOU201504060
            version,2
            info,visteam,CLE
            info,hometeam,HOU
            info,site,HOU03
            info,date,2015/04/06
            info,number,0
            info,starttime,6:10PM
            info,daynight,night
            info,usedh,true
            info,umphome,barrt901
            info,ump1b,herna901
            info,ump2b,barrs901
            info,ump3b,conrc901
            info,howscored,park
            info,pitches,pitches
            info,oscorer,blour701
            info,temp,79
            info,winddir,rtol
            info,windspeed,14
            info,fieldcond,unknown
            info,precip,unknown
            info,sky,cloudy
            info,timeofgame,150
            info,attendance,43753
            info,wp,keucd001
            info,lp,klubc001
            info,save,gregl001
            start,bourm001,"Michael Bourn",0,1,8
            start,kipnj001,"Jason Kipnis",0,2,4
            start,branm003,"Michael Brantley",0,3,7
            start,santc002,"Carlos Santana",0,4,3
            start,gomey001,"Yan Gomes",0,5,2
            start,mossb001,"Brandon Moss",0,6,9
            start,rabur001,"Ryan Raburn",0,7,10
            start,chisl001,"Lonnie Chisenhall",0,8,5
            start,ramij003,"Jose Ramirez",0,9,6
            start,klubc001,"Corey Kluber",0,0,1
            start,altuj001,"Jose Altuve",1,1,4
            start,sprig001,"George Springer",1,2,9
            start,valbl001,"Luis Valbuena",1,3,5
            start,gatte001,"Evan Gattis",1,4,10
            start,cartc002,"Chris Carter",1,5,3
            start,castj006,"Jason Castro",1,6,2
            start,lowrj001,"Jed Lowrie",1,7,6
            start,rasmc001,"Colby Rasmus",1,8,7
            start,marij002,"Jake Marisnick",1,9,8
            start,keucd001,"Dallas Keuchel",1,0,1
            play,1,0,bourm001,01,CX,43/G
            play,1,0,kipnj001,21,CBBX,7/F-
            play,1,0,branm003,11,CBX,3/G
            play,1,1,altuj001,22,BFBFX,63/G
            play,1,1,sprig001,00,X,8/F
            play,1,1,valbl001,02,CSX,43/G
            play,2,0,santc002,02,CCT,K
            play,2,0,gomey001,22,CBBSS,K
            play,2,0,mossb001,22,CSBFBS,K
            play,2,1,gatte001,32,SSBBBX,7/F
            play,2,1,cartc002,00,X,8/F
            play,2,1,castj006,12,CBTS,K
            play,3,0,rabur001,22,CBSBX,13/G-
            play,3,0,chisl001,01,CX,S7/G+
            play,3,0,ramij003,12,*BFSX,53/G.1-2
            play,3,0,bourm001,22,SSBBX,63/G
            play,3,1,lowrj001,11,CBX,31/G+
            play,3,1,rasmc001,31,SBBBB,W
            play,3,1,marij002,02,SCX,43/G-.1-2
            play,3,1,altuj001,22,B*BCFS,K
            play,4,0,kipnj001,01,CX,3/G+
            play,4,0,branm003,22,CBBCX,53/G
            play,4,0,santc002,32,CBBBF*B,W
            play,4,0,gomey001,12,CT*BX,1/L
            play,4,1,sprig001,32,BBCCBS,K
            play,4,1,valbl001,12,CFFBS,K
            play,4,1,gatte001,21,BFBX,7/L+
            play,5,0,mossb001,32,BBCFBB,W
            play,5,0,rabur001,22,BCS*BX,64(1)3/GDP
            play,5,0,chisl001,32,BBCBFB,W
            play,5,0,ramij003,22,C*BC*BX,13/G-
            play,5,1,cartc002,12,SSBX,9/F
            play,5,1,castj006,02,FFC,K
            play,5,1,lowrj001,32,BFBSBFX,43/G
            play,6,0,bourm001,12,SBFX,43/G-
            play,6,0,kipnj001,01,CX,13/G
            play,6,0,branm003,01,CX,7/L
            play,6,1,rasmc001,22,SBBCFC,K
            play,6,1,marij002,12,CFBFS,K
            play,6,1,altuj001,00,X,S8/L-
            play,6,1,sprig001,00,>C,SB2
            play,6,1,sprig001,12,>C.BCX,S7/G+.2-H;BX2(7534)
            play,7,0,santc002,01,CX,S7/L
            play,7,0,gomey001,00,X,S5/G.1-2
            play,7,0,mossb001,22,F*BCBFS,K
            play,7,0,rabur001,32,*BSB2BSFX,13/G-.2-3;1-2
            play,7,0,chisl001,00,X,13/G-
            play,7,1,valbl001,00,X,8/F
            play,7,1,gatte001,10,BX,43/G+
            play,7,1,cartc002,22,BFFBFFX,5/L
            play,8,0,ramij003,00,,NP
            sub,sippt001,"Tony Sipp",1,0,1
            play,8,0,ramij003,22,.BBCFS,K
            play,8,0,bourm001,22,CCBBFFS,K
            play,8,0,kipnj001,00,X,7/L
            play,8,1,castj006,10,BX,7/L
            play,8,1,lowrj001,32,BSBBCFB,W
            play,8,1,rasmc001,01,CX,S9/L.1-3
            play,8,1,marij002,00,,NP
            sub,atchs001,"Scott Atchison",0,0,1
            play,8,1,marij002,01,.CX,9/F/SF.3-H
            play,8,1,altuj001,21,1CB*B11X,64(1)/FO/G
            play,9,0,branm003,00,,NP
            sub,gregl001,"Luke Gregerson",1,0,1
            play,9,0,branm003,01,.CX,3/G
            play,9,0,santc002,00,X,43/G
            play,9,0,gomey001,02,CFS,K
            data,er,klubc001,2
            data,er,atchs001,0
            data,er,keucd001,0
            data,er,sippt001,0
            data,er,gregl001,0
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
        assertEquals("HOU201504060", game.id)
        assertEquals("CLE", game.awayTeam.teamId)
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
        assertEquals("Event: 6-4-3 Ground Ball Double Play Grounder", playerEvent.description)
        assertEquals(EventType.GROUND_BALL_DOUBLE_PLAY, playerEvent.eventType[0])
        assertEquals(EventType.GROUND_BALL, playerEvent.eventType[1])
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
}
