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

    @Test
    fun `should save all new teams`() {
        val team1 = Team("HOU", "Houston Astros", "AL", 1962, 2024)
        val team2 = Team("TEX", "Texas Rangers", "AL", 1961, 2024)

        whenever(teamRepository.findById("HOU")).thenReturn(java.util.Optional.empty())
        whenever(teamRepository.findById("TEX")).thenReturn(java.util.Optional.empty())

        teamService.saveAll(listOf(team1, team2))

        verify(teamRepository, times(1)).save(team1)
        verify(teamRepository, times(1)).save(team2)
    }

    @Test
    fun `should update existing teams`() {
        val existingTeam = Team("HOU", "Houston Astros", "AL", 1962, 2024)
        val updatedTeam = Team("HOU", "Houston Astros Updated", "AL", 1962, 2025)

        whenever(teamRepository.findById("HOU")).thenReturn(java.util.Optional.of(existingTeam))

        teamService.saveAll(listOf(updatedTeam))

        verify(teamRepository, times(1)).save(check {
            assertEquals("HOU", it.teamId)
            assertEquals("Houston Astros Updated", it.name)
            assertEquals(2025, it.lastYear)
        })
    }

    @Test
    fun `should handle mix of new and existing teams`() {
        val existingTeam = Team("HOU", "Houston Astros", "AL", 1962, 2024)
        val newTeam = Team("TEX", "Texas Rangers", "AL", 1961, 2024)

        whenever(teamRepository.findById("HOU")).thenReturn(java.util.Optional.of(existingTeam))
        whenever(teamRepository.findById("TEX")).thenReturn(java.util.Optional.empty())

        teamService.saveAll(listOf(existingTeam, newTeam))

        verify(teamRepository, times(1)).save(existingTeam)
        verify(teamRepository, times(1)).save(newTeam)
    }

    @Test
    fun `should do nothing when list is empty`() {
        teamService.saveAll(emptyList())

        verify(teamRepository, times(0)).save(any())
    }

    @Test
    fun `should retrieve all teams`() {
        val team1 = Team("HOU", "Houston Astros", "AL", 1962, 2024)
        val team2 = Team("TEX", "Texas Rangers", "AL", 1961, 2024)

        whenever(teamRepository.findAll()).thenReturn(listOf(team1, team2))

        val teams = teamService.findAll()

        assertNotNull(teams)
        assertEquals(2, teams.size)
        assertEquals("HOU", teams[0].teamId)
        assertEquals("TEX", teams[1].teamId)
    }
}
