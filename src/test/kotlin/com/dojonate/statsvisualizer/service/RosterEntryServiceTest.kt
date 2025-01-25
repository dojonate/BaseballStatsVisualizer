package com.dojonate.statsvisualizer.service

import com.dojonate.statsvisualizer.model.Player
import com.dojonate.statsvisualizer.model.RosterEntry
import com.dojonate.statsvisualizer.model.Team
import com.dojonate.statsvisualizer.repository.PlayerRepository
import com.dojonate.statsvisualizer.repository.RosterEntryRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.util.*

class RosterEntryServiceTest {

    private val rosterEntryRepository = mock<RosterEntryRepository>()
    private val playerRepository = mock<PlayerRepository>()
    private val rosterEntryService = RosterEntryService(rosterEntryRepository, playerRepository)

    @Test
    fun `should save all new roster entries`() {
        val player = Player("p001", "John", "Doe", "R", "L", null)
        val team = Team("HOU", "Houston Astros", "AL", 1962, 2024)
        val rosterEntry = RosterEntry(player, team, 2023, "P")

        whenever(playerRepository.findById(player.playerId)).thenReturn(Optional.empty())
        whenever(playerRepository.save(player)).thenReturn(player)
        whenever(rosterEntryRepository.findByPlayerAndTeamAndYear(player, team, 2023)).thenReturn(Optional.empty())
        whenever(rosterEntryRepository.save(rosterEntry)).thenReturn(rosterEntry)

        rosterEntryService.saveAll(listOf(rosterEntry))

        verify(playerRepository, times(1)).save(player)
        verify(rosterEntryRepository, times(1)).save(rosterEntry)
    }

    @Test
    fun `should skip saving existing roster entries`() {
        val player = Player("p001", "John", "Doe", "R", "L", null)
        val team = Team("HOU", "Houston Astros", "AL", 1962, 2024)
        val rosterEntry = RosterEntry(player, team, 2023, "P")

        whenever(playerRepository.findById(player.playerId)).thenReturn(Optional.of(player))
        whenever(rosterEntryRepository.findByPlayerAndTeamAndYear(player, team, 2023)).thenReturn(Optional.of(rosterEntry))

        rosterEntryService.saveAll(listOf(rosterEntry))

        verify(playerRepository, times(0)).save(any())
        verify(rosterEntryRepository, times(0)).save(any())
    }

    @Test
    fun `should retrieve all roster entries`() {
        val player = Player("p001", "John", "Doe", "R", "L", null)
        val team = Team("HOU", "Houston Astros", "AL", 1962, 2024)
        val rosterEntry = RosterEntry(player, team, 2023, "P")

        whenever(rosterEntryRepository.findAll()).thenReturn(listOf(rosterEntry))

        val entries = rosterEntryService.findAll()

        assertNotNull(entries)
        assertEquals(1, entries.size)
        assertEquals("p001", entries[0].player.playerId)
        assertEquals("HOU", entries[0].team.teamId)
    }
}
