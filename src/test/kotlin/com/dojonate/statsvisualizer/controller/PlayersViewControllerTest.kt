package com.dojonate.statsvisualizer.controller

import com.dojonate.statsvisualizer.config.SecurityConfig
import com.dojonate.statsvisualizer.model.Player
import com.dojonate.statsvisualizer.service.PlayerService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(PlayersViewController::class)
@Import(SecurityConfig::class, PlayersViewControllerTest.TestConfig::class)
class PlayersViewControllerTest {

    @TestConfiguration
    open class TestConfig {
        @Bean
        open fun playerService(): PlayerService = Mockito.mock(PlayerService::class.java)
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var playerService: PlayerService

    @Test
    fun `should return players view with paginated data`() {
        // Arrange
        val players = listOf(
            Player("p001", "John", "Doe", "R", "L", null),
            Player("p002", "Jane", "Smith", "L", "R", null)
        )
        val page =
            Mockito.mock(org.springframework.data.domain.Page::class.java) as org.springframework.data.domain.Page<Player>
        Mockito.`when`(page.content).thenReturn(players)
        Mockito.`when`(page.totalPages).thenReturn(3)
        Mockito.`when`(page.number).thenReturn(1)

        Mockito.`when`(playerService.getPlayers("", 1, 10, "lastName", "asc"))
            .thenReturn(page)

        // Act & Assert
        mockMvc.perform(
            get("/players")
                .param("page", "1")
                .param("size", "10")
                .param("sortBy", "lastName")
                .param("direction", "asc")
                .with(httpBasic("maintainer", "password"))
        )
            .andExpect(status().isOk)
            .andExpect(view().name("players"))
            .andExpect(model().attributeExists("players"))
            .andExpect(model().attribute("currentPage", 1))
            .andExpect(model().attribute("totalPages", 3))
    }
}
