package com.dojonate.statsvisualizer.service

import com.dojonate.statsvisualizer.model.Team
import com.dojonate.statsvisualizer.repository.TeamRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

class TeamServiceTest {

    private val teamRepository = mock<TeamRepository>() // Corrected mocking syntax
    private val teamService = TeamService(teamRepository)

    @Test
    fun `should create and save a new team`() {
        val team = Team("HOU", "Houston Astros", "AL", 1962, 2024)

        // Use argument matcher for mocking
        whenever(teamRepository.save(any())).thenReturn(team)

        val savedTeam = teamService.findOrCreateTeam("HOU", "Houston Astros", "AL")

        assertNotNull(savedTeam) // Verify team is not null
        assertEquals("HOU", savedTeam.teamId)
        assertEquals("Houston Astros", savedTeam.name)

        verify(teamRepository, times(1)).save(any()) // Verify save() was called once
    }

    @Test
    fun `should return existing team`() {
        val team = Team("HOU", "Houston Astros", "AL", 1962, 2024)

        whenever(teamRepository.findById("HOU")).thenReturn(java.util.Optional.of(team)) // Use `whenever`

        val existingTeam = teamService.findOrCreateTeam("HOU", "Unknown Team", "Unknown League")

        assertNotNull(existingTeam)
        assertEquals("HOU", existingTeam.teamId)
        assertEquals("Houston Astros", existingTeam.name)

        verify(teamRepository, times(0)).save(any())
    }
}
