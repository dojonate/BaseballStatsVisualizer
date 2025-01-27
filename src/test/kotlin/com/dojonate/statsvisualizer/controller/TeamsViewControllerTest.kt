package com.dojonate.statsvisualizer.controller

import com.dojonate.statsvisualizer.config.SecurityConfig
import com.dojonate.statsvisualizer.model.Team
import com.dojonate.statsvisualizer.service.TeamService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(TeamsViewController::class)
@Import(SecurityConfig::class, TeamsViewControllerTest.TestConfig::class)
class TeamsViewControllerTest {

    @TestConfiguration
    open class TestConfig {
        @Bean
        open fun teamService(): TeamService = Mockito.mock(TeamService::class.java)
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var teamService: TeamService

    @Test
    fun `should return teams view with list of teams`() {
        // Arrange
        val teams = listOf(
            Team("ACY", "Atlantic City Bacharach Giants", "ECL", 1916, 1929),
            Team("ANA", "Anaheim Angels", "AL", 1997, 2024)
        )
        val pageable = PageRequest.of(0, 10)
        val teamsPage: Page<Team> = PageImpl(teams, pageable, teams.size.toLong())
        Mockito.`when`(teamService.getTeams("", 0, 10, "name", "asc")).thenReturn(teamsPage)

        // Act & Assert
        mockMvc.perform(
            get("/teams")
                .with(httpBasic("maintainer", "password"))
        )
            .andExpect(status().isOk)
            .andExpect(view().name("teams"))
            .andExpect(model().attributeExists("teams"))
            .andExpect(model().attribute("teams", teams))
            .andExpect(model().attributeExists("currentPage"))
            .andExpect(model().attributeExists("totalPages"))
            .andExpect(model().attributeExists("search"))
    }
}