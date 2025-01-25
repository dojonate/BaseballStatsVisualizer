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
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(RosterUploadController::class)
@Import(RosterUploadControllerTest.TestConfig::class) // Import custom test configuration
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

    @Test
    fun `should upload a valid ROS file`() {
        val team = Team("HOU", "Houston Astros", "AL", 1962, 2024)
        whenever(teamService.findOrCreateTeam("HOU", "Unknown Team", "Unknown League")).thenReturn(team)

        val mockFile = MockMultipartFile(
            "file", "2011HOU.ROS", MediaType.TEXT_PLAIN_VALUE, "adamm001,Adams,Mike,R,R,HOU,P\n".toByteArray()
        )

        mockMvc.perform(MockMvcRequestBuilders.multipart("/rosters/upload")
            .file(mockFile)
            .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string("File uploaded and processed successfully."))
    }

    /**
     * Custom configuration for injecting mocked beans.
     */
    @TestConfiguration
    class TestConfig {

        @Bean
        fun teamService(): TeamService {
            return mock(TeamService::class.java)
        }

        @Bean
        fun rosterEntryService(): RosterEntryService {
            return mock(RosterEntryService::class.java)
        }

        @Bean
        fun rosFileParser(): RosFileParser {
            return mock(RosFileParser::class.java)
        }
    }
}
