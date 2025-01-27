package com.dojonate.statsvisualizer.controller

import com.dojonate.statsvisualizer.config.SecurityConfig
import com.dojonate.statsvisualizer.model.Player
import com.dojonate.statsvisualizer.model.RosterEntry
import com.dojonate.statsvisualizer.model.Team
import com.dojonate.statsvisualizer.service.RosterEntryService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageImpl
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDate

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
        val rosters = listOf(
            RosterEntry(
                Player("jsmith001", "John", "Smith", "R", "R", LocalDate.of(1995, 1, 1)),
                Team("TEX", "Texas Rangers", "AL", 1971, 2024),
                2020,
                "P"
            ),
            RosterEntry(
                Player("jdoe001", "Jane", "Doe", "L", "R", LocalDate.of(1996, 2, 2)),
                Team("NYM", "New York Mets", "NL", 1962, 2024),
                2020,
                "C"
            )
        )
        Mockito.`when`(rosterEntryService.getRosters("", 0, 10, "playerName", "asc")).thenReturn(PageImpl(rosters))

        // Act & Assert
        mockMvc.perform(
            get("/rosters")
                .with(httpBasic("maintainer", "password"))
        )
            .andExpect(status().isOk)
            .andExpect(view().name("rosters"))
            .andExpect(model().attributeExists("rosters"))
            .andExpect(model().attribute("rosters", rosters))
    }
}