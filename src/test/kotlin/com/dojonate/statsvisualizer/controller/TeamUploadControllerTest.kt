package com.dojonate.statsvisualizer.controller

import com.dojonate.statsvisualizer.repository.TeamRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TeamUploadControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val teamRepository: TeamRepository
) {

    companion object {
        private const val VALID_TEAM_CSV = """TEAM,LEAGUE,CITY,NICKNAME,FIRST,LAST
            HOU,NL,Houston,Astros,1962,2024
            TEX,AL,Texas,Rangers,1961,2024"""
    }

    @BeforeEach
    fun setup() {
        teamRepository.deleteAll() // Clear the repository before each test
    }

    @Test
    fun `should upload team CSV and save data to database`() {
        // Arrange
        val mockFile = createMockFile("teams.csv", VALID_TEAM_CSV)

        // Act
        performFileUpload(mockFile)
            .andExpect(status().isOk)

        // Assert
        val teams = teamRepository.findAll()
        assertEquals(2, teams.size, "Expected two teams to be saved in the database.")
        assertEquals("Houston Astros", teams.first { it.teamId == "HOU" }.name, "Team name for 'HOU' does not match.")
    }

    private fun createMockFile(fileName: String, content: String): MockMultipartFile {
        return MockMultipartFile(
            "file",
            fileName,
            MediaType.TEXT_PLAIN_VALUE,
            content.toByteArray()
        )
    }

    private fun performFileUpload(file: MockMultipartFile): ResultActions {
        return mockMvc.perform(
            MockMvcRequestBuilders.multipart("/upload/teams")
                .file(file)
                .with(csrf()) // Add CSRF token
                .with(httpBasic("maintainer", "password")) // Add basic authentication
                .contentType(MediaType.MULTIPART_FORM_DATA)
        )
    }
}