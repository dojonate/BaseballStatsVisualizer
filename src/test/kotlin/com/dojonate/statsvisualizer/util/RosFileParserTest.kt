package com.dojonate.statsvisualizer.util

import com.dojonate.statsvisualizer.model.Team
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class RosFileParserTest {

    private val rosFileParser = RosFileParser()

    @Test
    fun `should parse valid ROS file`() {
        val team = Team("HOU", "Houston Astros", "AL", 1962, 2024)
        val tempFile = createTempFile("HOU2011.ROS", "adamm001,Adams,Mike,R,R,HOU,P\n")

        val rosterEntries = rosFileParser.parseRosFile(tempFile, team)

        assertEquals(1, rosterEntries.size)
        assertEquals("adamm001", rosterEntries[0].player.playerId)
        assertEquals("HOU", rosterEntries[0].team.teamId)
        assertEquals("P", rosterEntries[0].position)
        assertEquals(2011, rosterEntries[0].year)
    }

    @Test
    fun `should handle invalid lines gracefully`() {
        val team = Team("HOU", "Houston Astros", "AL", 1962, 2024)
        val tempFile = createTempFile("HOU2011.ROS", "invalid_line\nadamm001,Adams,Mike,R,R,HOU,P\n")

        val rosterEntries = rosFileParser.parseRosFile(tempFile, team)

        assertEquals(1, rosterEntries.size)
        assertEquals("adamm001", rosterEntries[0].player.playerId)
    }

    @Test
    fun `should throw exception for invalid year in filename`() {
        val team = Team("HOU", "Houston Astros", "AL", 1962, 2024)
        val tempFile = createTempFile("HOUXXXX.ROS", "adamm001,Adams,Mike,R,R,HOU,P\n")

        assertThrows<NumberFormatException> {
            rosFileParser.parseRosFile(tempFile, team)
        }
    }

    private fun createTempFile(fileName: String, content: String): Path {
        val tempFile = Files.createTempFile(fileName, fileName)
        Files.write(tempFile, content.toByteArray(), StandardOpenOption.WRITE)
        return tempFile
    }
}
