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
import kotlin.test.assertContains

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
        validEventFileContent = """
            id,HOU201504300
            version,2
            info,visteam,SEA
            info,hometeam,HOU
            info,site,HOU03
            info,date,2015/04/30
            info,number,0
            info,starttime,7:11PM
            info,daynight,night
            info,usedh,true
            info,umphome,carav901
            info,ump1b,vanol901
            info,ump2b,kulpr901
            info,ump3b,knigb901
            info,howscored,park
            info,pitches,pitches
            info,oscorer,porzg701
            info,temp,80
            info,winddir,fromlf
            info,windspeed,5
            info,fieldcond,unknown
            info,precip,unknown
            info,sky,sunny
            info,timeofgame,181
            info,attendance,19108
            info,wp,gregl001
            info,lp,leond003
            info,save,
            start,smits002,"Seth Smith",0,1,9
            start,jacka001,"Austin Jackson",0,2,8
            start,canor001,"Robinson Cano",0,3,4
            start,cruzn002,"Nelson Cruz",0,4,10
            start,seagk001,"Kyle Seager",0,5,5
            start,morrl001,"Logan Morrison",0,6,3
            start,millb002,"Brad Miller",0,7,6
            start,ackld001,"Dustin Ackley",0,8,7
            start,zunim001,"Mike Zunino",0,9,2
            start,paxtj001,"James Paxton",0,0,1
            start,altuj001,"Jose Altuve",1,1,4
            start,valbl001,"Luis Valbuena",1,2,5
            start,sprig001,"George Springer",1,3,9
            start,gatte001,"Evan Gattis",1,4,10
            start,cartc002,"Chris Carter",1,5,3
            start,castj006,"Jason Castro",1,6,2
            start,marij002,"Jake Marisnick",1,7,8
            start,gonzm002,"Marwin Gonzalez",1,8,6
            start,grosr001,"Robbie Grossman",1,9,7
            start,felds001,"Scott Feldman",1,0,1
            play,1,0,smits002,12,CFBX,S5/G
            play,1,0,jacka001,02,FCFB,WP.1-2
            play,1,0,jacka001,12,FCFB.X,S6/G.2-2
            play,1,0,canor001,01,CX,S6/G.2-3;1-2
            play,1,0,cruzn002,00,X,64(1)3/GDP.3-H(NR);2-3
            play,1,0,seagk001,22,CBBCX,S7/L.3-H
            play,1,0,morrl001,11,BSX,7/F
            play,1,1,altuj001,01,CX,S9/L
            play,1,1,valbl001,31,1FBBB>C,SB2
            play,1,1,valbl001,32,1FBBB>C.B,W
            play,1,1,sprig001,22,CBF*BX,53/G.2-3;1-2
            play,1,1,gatte001,11,FBX,43/G.3-H;2-3
            play,1,1,cartc002,11,BSX,7/F
            play,2,0,millb002,30,BBBB,W
            play,2,0,ackld001,01,CB,WP.1-2
            play,2,0,ackld001,21,CB.BX,23/G.2-3
            play,2,0,zunim001,11,BFX,S5/G.3-3
            play,2,0,smits002,00,X,46(1)3/GDP
            play,2,1,castj006,10,BX,S8/G
            play,2,1,marij002,00,X,S1/BG.1-2
            play,2,1,gonzm002,32,BBSFBX,64(1)3/GDP.2-3
            play,2,1,grosr001,22,CF*BBC,K
            play,3,0,jacka001,00,X,S5/BG
            play,3,0,canor001,02,C1F>B,CS2(26)
            play,3,0,canor001,12,C1F>B.X,63/G
            play,3,0,cruzn002,12,CSBX,8/F
            play,3,1,altuj001,21,BBCX,D7/L
            play,3,1,valbl001,12,BFCS,K
            play,3,1,sprig001,21,BBFX,43/G.2-3
            play,3,1,gatte001,22,TBS*BFS,K
            play,4,0,seagk001,01,CX,13/G
            play,4,0,morrl001,12,FBFX,43/G
            play,4,0,millb002,31,BSBBB,W
            play,4,0,ackld001,00,X,S7/L.1-2
            play,4,0,zunim001,22,S2BFFBFFFS,K23
            play,4,1,cartc002,12,CCBS,K
            play,4,1,castj006,32,FBCFBBS,K
            play,4,1,marij002,12,BFSX,4/L
            play,5,0,smits002,32,CBBBFS,K
            play,5,0,jacka001,32,BCBSBX,63/G
            play,5,0,canor001,00,X,S9/L
            play,5,0,cruzn002,11,CBX,13/G
            play,5,1,gonzm002,10,BX,53/G
            play,5,1,grosr001,00,X,53/G
            play,5,1,altuj001,22,BCCBX,63/G
            play,6,0,seagk001,21,CBBX,31/G
            play,6,0,morrl001,21,BBFX,7/L
            play,6,0,millb002,32,BCFBFBX,9/L
            play,6,1,valbl001,32,CBCBBB,W
            play,6,1,sprig001,22,MF*B1BX,S9/L.1-3;B-2(TH3)
            play,6,1,gatte001,11,F*BX,53/G.3-H
            play,6,1,cartc002,12,CFBC,K
            play,6,1,castj006,00,X,8/F
            play,7,0,ackld001,21,CBBX,6/P
            play,7,0,zunim001,00,X,8/L
            play,7,0,smits002,11,BCX,31/G
            play,7,1,marij002,10,BX,63/G
            play,7,1,gonzm002,10,BX,53/G
            play,7,1,grosr001,32,BFCBBC,K
            play,8,0,jacka001,00,,NP
            sub,qualc001,"Chad Qualls",1,0,1
            play,8,0,jacka001,22,.BCSBX,S9/G
            play,8,0,canor001,01,F>B,SB2
            play,8,0,canor001,11,F>B.X,13/G
            play,8,0,cruzn002,02,STFS,K
            play,8,0,seagk001,00,,NP
            sub,sippt001,"Tony Sipp",1,0,1
            play,8,0,seagk001,01,.S*B,SB3
            play,8,0,seagk001,22,.S*B.BFFX,43/G
            play,8,1,altuj001,00,,NP
            sub,smitc004,"Carson Smith",0,0,1
            play,8,1,altuj001,01,.CX,53/G
            play,8,1,valbl001,11,BFX,31/G
            play,8,1,sprig001,11,FBX,43/G
            play,9,0,morrl001,12,CBCX,S9/G
            play,9,0,millb002,11,LBX,14/BG/SH.1-2
            play,9,0,ackld001,00,B,PB.2-3
            play,9,0,ackld001,12,B.CFFS,K
            play,9,0,zunim001,00,,NP
            sub,neshp001,"Pat Neshek",1,0,1
            play,9,0,zunim001,02,.CSX,4/P
            play,9,1,gatte001,00,,NP
            sub,leond003,"Dominic Leone",0,0,1
            play,9,1,gatte001,12,.SSBX,53/G
            play,9,1,cartc002,22,SBFBS,K
            play,9,1,castj006,32,CFBFBBX,43/G
            play,10,0,smits002,00,,NP
            sub,gregl001,"Luke Gregerson",1,0,1
            play,10,0,smits002,12,.CTBFFFFX,S9/G
            play,10,0,jacka001,00,,NP
            sub,ruggj001,"Justin Ruggiano",0,1,12
            play,10,0,jacka001,01,.MX,3/BP
            play,10,0,canor001,00,X,16(1)3/GDP
            play,10,1,marij002,00,,NP
            sub,ruggj001,"Justin Ruggiano",0,1,9
            play,10,1,marij002,01,.CX,63/G
            play,10,1,gonzm002,00,X,D9/G
            play,10,1,grosr001,00,,NP
            sub,rasmc001,"Colby Rasmus",1,9,11
            play,10,1,rasmc001,31,.*BB*BCB,W
            play,10,1,altuj001,10,BX,S7/L.2-H;1-3
            data,er,paxtj001,2
            data,er,smitc004,0
            data,er,leond003,1
            data,er,felds001,2
            data,er,qualc001,0
            data,er,sippt001,0
            data,er,neshp001,0
            data,er,gregl001,0
        """.trimIndent()

        invalidEventFileContent = """
            id,HOUXXXXXXX
            play,1,0,choos001,10,BX,S8.2H;1-3
        """.trimIndent() // Invalid ID and malformed runner advances
    }

    fun countPlayLines(input: String?): Int {
        if (input.isNullOrEmpty()) {
            return 0
        }

        return input.lines().count { it.startsWith("play,") }
    }

    @Test
    fun `should parse valid event file`() {
        val game = eventFileParser.parse(validEventFileContent)

        // Assertions for basic game properties
        assertEquals("HOU201504300", game.id)
        assertEquals("SEA", game.awayTeam.teamId)
        assertEquals("HOU", game.homeTeam.teamId)
        assertEquals(countPlayLines(validEventFileContent), game.playerEvents.size)

        // Assertions for specific PlayerEvents
        val firstEvent = game.playerEvents[0]
        assertContains(firstEvent.eventType, EventType.GROUND_BALL)

        val secondEvent = game.playerEvents[14]
        assertContains(secondEvent.eventType, EventType.WILD_PITCH)
        assertEquals("1-2", secondEvent.runnerAdvances[0].baseMovement)
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
    fun `should handle triple plays`() {
        val modifierEventContent = """
            id,HOU20230617
            play,7,1,randw001,00,.>X,1(B)16(2)63(1)/LTP/L1
        """.trimIndent()

        val game = eventFileParser.parse(modifierEventContent)
        val playerEvent = game.playerEvents.first()

        // Validate event details
        assertEquals("Event: 1-6-3 Lined Into Triple Play Line Drive", playerEvent.description)
        assertEquals(EventType.LINED_INTO_TRIPLE_PLAY, playerEvent.eventType[0])
        assertEquals(EventType.LINE_DRIVE, playerEvent.eventType[1])
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

        assertEquals("Invalid runner advance base movement format: 2H", exception.message)
    }
}
