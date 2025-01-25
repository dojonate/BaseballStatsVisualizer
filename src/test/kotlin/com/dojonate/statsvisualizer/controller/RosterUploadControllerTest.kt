package com.dojonate.statsvisualizer.controller

import com.dojonate.statsvisualizer.model.Team
import com.dojonate.statsvisualizer.service.RosterEntryService
import com.dojonate.statsvisualizer.service.TeamService
import com.dojonate.statsvisualizer.util.RosFileParser
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(RosterUploadController::class)
@Import(RosterUploadControllerTest.TestConfig::class, com.dojonate.statsvisualizer.config.SecurityConfig::class)
class RosterUploadControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var teamService: TeamService

    @Autowired
    private lateinit var rosterEntryService: RosterEntryService

    @Autowired
    private lateinit var rosFileParser: RosFileParser

    @Value("\${file.upload.temp-dir}")
    private lateinit var tempDir: String

    companion object {
        private const val VALID_ROS_CSV = "adamm001,Adams,Mike,R,R,HOU,P\n" // Moved CSV content to a constant
    }

    @Test
    fun `should upload a valid ROS file`() {
        val expectedTeam = Team("HOU", "Houston Astros", "AL", 1962, 2024) // Renamed variable for clarity
        whenever(teamService.findOrCreateTeam("HOU", "Unknown Team", "Unknown League")).thenReturn(expectedTeam)

        val mockFile = createMockFile("2011HOU.ROS", VALID_ROS_CSV)

        performFileUpload(mockFile)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string("File uploaded and processed successfully."))
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
            MockMvcRequestBuilders.multipart("/upload/rosters")
                .file(file)
                .with(csrf())
                .with(httpBasic("maintainer", "password"))
                .contentType(MediaType.MULTIPART_FORM_DATA)
        )
    }

    @TestConfiguration
    open class TestConfig {

        @Bean
        open fun teamService(): TeamService = mock(TeamService::class.java)

        @Bean
        open fun rosterEntryService(): RosterEntryService = mock(RosterEntryService::class.java)

        @Bean
        open fun rosFileParser(): RosFileParser = mock(RosFileParser::class.java)
    }
}