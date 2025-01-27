package com.dojonate.statsvisualizer.controller

import com.dojonate.statsvisualizer.config.SecurityConfig
import com.dojonate.statsvisualizer.model.Player
import com.dojonate.statsvisualizer.model.Team
import com.dojonate.statsvisualizer.service.RosterEntryService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDate
import java.util.AbstractMap.SimpleEntry

@WebMvcTest(RostersViewController::class)
@Import(SecurityConfig::class, RostersViewControllerTest.TestConfig::class)
class RostersViewControllerTest {

    @TestConfiguration
    open class TestConfig {
        @Bean
        open fun rosterEntryService(): RosterEntryService = Mockito.mock(RosterEntryService::class.java)
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var rosterEntryService: RosterEntryService

    @Test
    fun `should return rosters view with list of rosters`() {
        // Arrange
        val player1 = Player("jsmith001", "John", "Smith", "R", "R", LocalDate.of(1995, 1, 1))
        val player2 = Player("jdoe001", "Jane", "Doe", "L", "R", LocalDate.of(1996, 2, 2))
        val team1 = Team("TEX", "Texas Rangers", "AL", 1971, 2024)
        val team2 = Team("NYM", "New York Mets", "NL", 1962, 2024)
        val consolidatedRosters = listOf(
            SimpleEntry(player1, mapOf(team1 to SimpleEntry("P", "2020"))),
            SimpleEntry(player2, mapOf(team2 to SimpleEntry("C", "2020")))
        )
        val page: Page<Map.Entry<Player, Map<Team, Map.Entry<String, String>>>> = PageImpl(consolidatedRosters)

        Mockito.`when`(rosterEntryService.getConsolidatedRosters("", 0, 10, "player.lastName", "asc")).thenReturn(page)
        // Act & Assert
        mockMvc.perform(
            get("/rosters")
                .with(httpBasic("maintainer", "password"))
        )
            .andExpect(status().isOk)
            .andExpect(view().name("rosters"))
            .andExpect(model().attributeExists("consolidatedRosters"))
            .andExpect(model().attribute("consolidatedRosters", page))
    }
}