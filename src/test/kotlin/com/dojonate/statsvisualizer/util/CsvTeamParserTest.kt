package com.dojonate.statsvisualizer.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class CsvTeamParserTest {

    private val csvTeamParser = CsvTeamParser()

    @Test
    fun `should parse valid team CSV file`() {
        val tempFile = createTempFile(
            "teams.csv",
            "TEAM,LEAGUE,CITY,NICKNAME,FIRST,LAST\nHOU,AL,Houston,Astros,1962,2024\nTEX,AL,Texas,Rangers,1961,2024\n"
        )

        val teams = csvTeamParser.parseTeamCsv(tempFile)

        assertEquals(2, teams.size)
        val team1 = teams.first { it.teamId == "HOU" }
        assertEquals("Houston Astros", team1.name)
        assertEquals("AL", team1.league)
        assertEquals(1962, team1.firstYear)
        assertEquals(2024, team1.lastYear)

        val team2 = teams.first { it.teamId == "TEX" }
        assertEquals("Texas Rangers", team2.name)
    }

    @Test
    fun `should handle invalid lines gracefully`() {
        val tempFile = createTempFile(
            "teams.csv",
            "TEAM,LEAGUE,CITY,NICKNAME,FIRST,LAST\nINVALID_LINE\nHOU,AL,Houston,Astros,1962,2024\n"
        )

        val teams = csvTeamParser.parseTeamCsv(tempFile)

        assertEquals(1, teams.size)
        assertEquals("HOU", teams[0].teamId)
    }

    @Test
    fun `should return empty list for empty file`() {
        val tempFile = createTempFile("empty.csv", "")

        val teams = csvTeamParser.parseTeamCsv(tempFile)

        assertTrue(teams.isEmpty())
    }

    @Test
    fun `should return empty list for malformed file`() {
        val tempFile = createTempFile("malformed.csv", "TEAM,LEAGUE,CITY\\nHOU,AL,Houston\\n")

        val teams = csvTeamParser.parseTeamCsv(tempFile)

        assertTrue(teams.isEmpty())
    }

    private fun createTempFile(fileName: String, content: String): Path {
        val tempFile = Files.createTempFile(fileName, null)
        Files.write(tempFile, content.toByteArray(), StandardOpenOption.WRITE)
        return tempFile
    }
}