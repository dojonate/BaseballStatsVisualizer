package com.dojonate.statsvisualizer.controller

import com.dojonate.statsvisualizer.model.Game
import com.dojonate.statsvisualizer.model.PlayerEvent
import com.dojonate.statsvisualizer.service.GameService
import com.dojonate.statsvisualizer.service.PlayerEventService
import com.dojonate.statsvisualizer.service.PlayerService
import com.dojonate.statsvisualizer.service.TeamService
import com.dojonate.statsvisualizer.util.EventFileParser
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(EventUploadController::class)
@Import(EventUploadControllerTest.Config::class, com.dojonate.statsvisualizer.config.SecurityConfig::class)
class EventUploadControllerTest {

    @TestConfiguration
    open class Config {
        @Bean
        @Primary
        open fun eventUploadController(): EventUploadController =
            EventUploadController(eventFileParser(), gameService(), playerEventService())

        @Bean
        open fun eventFileParser(): EventFileParser = mock(EventFileParser::class.java)

        @Bean
        open fun gameService(): GameService = mock(GameService::class.java)

        @Bean
        open fun playerEventService(): PlayerEventService = mock(PlayerEventService::class.java)

        @Bean
        open fun playerService(): PlayerService = mock(PlayerService::class.java)

        @Bean
        open fun mockTeamService(): TeamService = mock(TeamService::class.java)
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var eventFileParser: EventFileParser

    @Autowired
    private lateinit var gameService: GameService

    @Autowired
    private lateinit var playerEventService: PlayerEventService

    @Autowired
    private lateinit var eventUploadController: EventUploadController

    @Test
    fun `upload valid event file returns success`() {
        // Arrange: Create dummy file content and a Game with at least one PlayerEvent
        val fileContent = """
            id,HOU201504060
            info,visteam,CLE
            info,hometeam,HOU
            play,1,0,player1,01,CX,S8.3-H;1-2
        """.trimIndent()

        val game = Game().apply {
            id = "HOU201504060"
            playerEvents = listOf(mock<PlayerEvent>())
        }
        whenever(eventFileParser.parse(any())).thenReturn(game)
        whenever(gameService.save(any())).thenReturn(game)
        whenever(playerEventService.saveAll(any())).thenReturn(game.playerEvents)

        val mockFile = MockMultipartFile("file", "test.eve", MediaType.TEXT_PLAIN_VALUE, fileContent.toByteArray())

        // Act & Assert
        mockMvc.perform(multipart("/upload/events").file(mockFile).with(httpBasic("maintainer", "password")))
            .andExpect(status().isOk)
            .andExpect(content().string("File uploaded successfully."))
    }

    @Test
    fun `upload empty file returns bad request`() {
        // Arrange: Create an empty multipart file
        val mockFile = MockMultipartFile("file", "empty.eve", MediaType.TEXT_PLAIN_VALUE, ByteArray(0))

        // Act & Assert
        mockMvc.perform(multipart("/upload/events").file(mockFile).with(httpBasic("maintainer", "password")))
            .andExpect(status().isBadRequest)
    }
}
