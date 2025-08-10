package com.dojonate.statsvisualizer.util

import com.dojonate.statsvisualizer.model.Team
import com.dojonate.statsvisualizer.util.RosFileParser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

class RetrosheetEventParserTest {

    private val parser = RetrosheetEventParser()

    @Test
    fun `should parse event file with plays`() {
        val content = """
            id,EXAMPLE1
            info,visteam,HOU
            info,hometeam,TEX
            play,1,0,aloma001,??,S,S7/G
            play,1,1,adamm001,??,X,HR/7
        """.trimIndent()

        val tempFile = createTempFile("example.evn", content)

        val game = parser.parseEventFile(tempFile)

        assertEquals("EXAMPLE1", game.gameId)
        assertEquals("HOU", game.info["visteam"])
        assertEquals(2, game.plays.size)
        assertEquals(1, game.plays[0].inning)
        assertEquals(false, game.plays[0].isHomeTeam())
        assertEquals("aloma001", game.plays[0].playerId)
        assertEquals("HR/7", game.plays[1].event)
    }

    @Test
    fun `should parse sample Retrosheet file`() {
        val resource = javaClass.getResource("/retrosheet/BOS19300415.EVA")!!
        val path = Paths.get(resource.toURI())

        val game = parser.parseEventFile(path)

        assertEquals("BOS193004150", game.gameId)
        assertEquals("WS1", game.info["visteam"])
        assertEquals(75, game.plays.size)
        assertEquals("wests101", game.plays.first().playerId)
        assertEquals("E5/G", game.plays.first().event)
    }

    @Test
    fun `should ignore comments and blank lines`() {
        val content = """
            id,EXAMPLE2

            # comment line
            play,1,0,aloma001,??,S,S7/G
        """.trimIndent()

        val tempFile = createTempFile("example2.evn", content)
        val game = parser.parseEventFile(tempFile)

        assertEquals(1, game.plays.size)
        assertEquals("aloma001", game.plays.first().playerId)
    }

    @Test
    fun `should parse runner advances`() {
        val content = """
            id,EXAMPLE3
            play,1,0,aloma001,??,S,S7/G.1-2;B-1
            play,1,0,smith001,??,S,S7/G.1X2
        """.trimIndent()

        val tempFile = createTempFile("example3.evn", content)
        val game = parser.parseEventFile(tempFile)

        assertEquals("2", game.plays[0].runnerAdvances['1'])
        assertEquals("1", game.plays[0].runnerAdvances['B'])
        assertEquals("X2", game.plays[1].runnerAdvances['1'])
        assertEquals("S7/G", game.plays[0].event)
    }

    @Test
    fun `should parse starting lineups with roster cross reference`() {
        val event = Paths.get(javaClass.getResource("/retrosheet/BOS19300415.EVA")!!.toURI())
        val bosTeam = Team("BOS", "Boston Red Sox", "AL", null, null)
        val wsTeam = Team("WS1", "Washington Senators", "AL", null, null)
        val rosParser = RosFileParser()
        val bosRoster = rosParser.parseRosFile(Paths.get(javaClass.getResource("/retrosheet/BOS1930.ROS")!!.toURI()), bosTeam)
        val wsRoster = rosParser.parseRosFile(Paths.get(javaClass.getResource("/retrosheet/WS11930.ROS")!!.toURI()), wsTeam)
        val rosters = mapOf("BOS" to bosRoster, "WS1" to wsRoster)

        val game = parser.parseEventFile(event, rosters)

        assertEquals(9, game.visitorLineup.size)
        assertEquals(9, game.homeLineup.size)

        val westRosterPlayer = wsRoster.first { it.player.playerId == "wests101" }.player
        val westLineup = game.visitorLineup.first()
        assertSame(westRosterPlayer, westLineup.player)
        assertEquals(1, westLineup.battingOrder)
        assertEquals(8, westLineup.fieldingPosition)

        val rothRosterPlayer = bosRoster.first { it.player.playerId == "rothj101" }.player
        val rothLineup = game.homeLineup.first()
        assertSame(rothRosterPlayer, rothLineup.player)
        assertEquals(1, rothLineup.battingOrder)
        assertEquals(9, rothLineup.fieldingPosition)
    }

    private fun createTempFile(fileName: String, content: String): Path {
        val tempFile = Files.createTempFile(fileName, fileName)
        Files.write(tempFile, content.toByteArray(), StandardOpenOption.WRITE)
        return tempFile
    }
}
