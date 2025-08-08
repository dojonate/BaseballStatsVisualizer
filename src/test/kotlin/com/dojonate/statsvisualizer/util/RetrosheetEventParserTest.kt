package com.dojonate.statsvisualizer.util

import org.junit.jupiter.api.Assertions.assertEquals
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

    private fun createTempFile(fileName: String, content: String): Path {
        val tempFile = Files.createTempFile(fileName, fileName)
        Files.write(tempFile, content.toByteArray(), StandardOpenOption.WRITE)
        return tempFile
    }
}
