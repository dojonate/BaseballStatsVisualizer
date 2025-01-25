package com.dojonate.statsvisualizer.controller

import com.dojonate.statsvisualizer.repository.TeamRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Activates application-test.properties
class TeamUploadControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val teamRepository: TeamRepository
) {

    @BeforeEach
    fun setup() {
        teamRepository.deleteAll() // Clear the repository before each test
    }

    @Test
    fun `should upload team CSV and save data to database`() {
        // Arrange
        val csvContent = """TEAM,LEAGUE,CITY,NICKNAME,FIRST,LAST
            HOU,NL,Astros,Houston,1962,2024
            TEX,AL,Rangers,Texas,1961,2024"""

        val csvFile = MockMultipartFile(
            "file", "teams.csv", "text/csv", csvContent.toByteArray()
        )

        // Act
        mockMvc.perform(
            multipart("/teams/upload").file(csvFile)
        )
            .andExpect(status().isOk)

        // Assert
        val teams = teamRepository.findAll()
        assertEquals(2, teams.size) // Verify 2 teams are saved
        assertEquals("Houston Astros", teams.first { it.teamId == "HOU" }.name)
    }
}
