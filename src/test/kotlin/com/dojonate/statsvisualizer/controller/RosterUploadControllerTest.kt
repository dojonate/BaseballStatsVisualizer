package com.dojonate.statsvisualizer.controller

import com.dojonate.statsvisualizer.model.Team
import com.dojonate.statsvisualizer.service.RosterEntryService
import com.dojonate.statsvisualizer.service.TeamService
import com.dojonate.statsvisualizer.util.RosFileParser
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(RosterUploadController::class)
class RosterUploadControllerTest(
    private val mockMvc: MockMvc,
    private val teamService: TeamService,
    private val rosterEntryService: RosterEntryService,
    private val rosFileParser: RosFileParser,
    @Value("\${file.upload.temp-dir}") private val tempDir: String
) {

    @Test
    fun `should upload a valid ROS file`() {
        val team = Team("HOU", "Houston Astros", "AL", 1962, 2024)
        `when`(teamService.findOrCreateTeam("HOU", "Unknown Team", "Unknown League")).thenReturn(team)

        val mockFile = MockMultipartFile(
            "file", "2011HOU.ROS", MediaType.TEXT_PLAIN_VALUE, "adamm001,Adams,Mike,R,R,HOU,P\n".toByteArray()
        )

        mockMvc.perform(MockMvcRequestBuilders.multipart("/rosters/upload")
            .file(mockFile)
            .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string("File uploaded and processed successfully."))
    }
}
