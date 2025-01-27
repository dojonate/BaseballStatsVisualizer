package com.dojonate.statsvisualizer.service

import com.dojonate.statsvisualizer.model.Player
import com.dojonate.statsvisualizer.model.RosterEntry
import com.dojonate.statsvisualizer.model.Team
import com.dojonate.statsvisualizer.repository.PlayerRepository
import com.dojonate.statsvisualizer.repository.RosterEntryRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import java.time.LocalDate
import java.util.*

class RosterEntryServiceTest {

    private val rosterEntryRepository = mock<RosterEntryRepository>()
    private val playerRepository = mock<PlayerRepository>()
    private val rosterEntryService = RosterEntryService(rosterEntryRepository, playerRepository)

    @Test
    fun `should retrieve paginated roster entries`() {
        val rosterEntries = listOf(
            RosterEntry(
                Player("jdoe001", "Jane", "Doe", "L", "R", LocalDate.of(1996, 2, 2)),
                Team("NYM", "New York Mets", "NL", 1962, 2024), 2020, "Catcher"
            ),
            RosterEntry(
                Player("jsmith001", "John", "Smith", "R", "R", LocalDate.of(1995, 1, 1)),
                Team("TEX", "Texas Rangers", "AL", 1971, 2024), 2020, "Pitcher"
            )
        )
        val pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "player.lastName")
        val pagedResult = PageImpl(rosterEntries, pageable, rosterEntries.size.toLong())

        whenever(
            rosterEntryRepository.findByPlayerFirstNameContainingOrPlayerLastNameContainingOrTeamNameContaining(
                "",
                "",
                "",
                pageable
            )
        ).thenReturn(
            pagedResult
        )

        val result = rosterEntryService.getRosters("", 0, 10, "player.lastName", "asc")

        assertNotNull(result)
        assertEquals(2, result.content.size)
        assertEquals("Doe", result.content[0].player.lastName)
        assertEquals("Smith", result.content[1].player.lastName)
    }

    @Test
    fun `should save all roster entries`() {
        val player1 = Player("jsmith001", "John", "Smith", "R", "R", LocalDate.of(1995, 1, 1))
        val player2 = Player("jdoe001", "Jane", "Doe", "L", "R", LocalDate.of(1996, 2, 2))
        val team1 = Team("TEX", "Texas Rangers", "AL", 1971, 2024)
        val team2 = Team("NYM", "New York Mets", "NL", 1962, 2024)
        val rosterEntry1 = RosterEntry(player1, team1, 2020, "Pitcher")
        val rosterEntry2 = RosterEntry(player2, team2, 2020, "Catcher")

        whenever(playerRepository.findById(player1.playerId)).thenReturn(Optional.of(player1))
        whenever(playerRepository.findById(player2.playerId)).thenReturn(Optional.empty())
        whenever(playerRepository.save(player2)).thenReturn(player2)
        whenever(rosterEntryRepository.findByPlayerAndTeamAndYear(player1, team1, 2020)).thenReturn(Optional.empty())
        whenever(rosterEntryRepository.findByPlayerAndTeamAndYear(player2, team2, 2020)).thenReturn(Optional.empty())

        rosterEntryService.saveAll(listOf(rosterEntry1, rosterEntry2))

        verify(playerRepository, times(1)).findById(player1.playerId)
        verify(playerRepository, times(1)).findById(player2.playerId)
        verify(playerRepository, times(1)).save(player2)
        verify(rosterEntryRepository, times(1)).save(rosterEntry1)
        verify(rosterEntryRepository, times(1)).save(rosterEntry2)
    }
}