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
import org.mockito.kotlin.whenever
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import java.time.LocalDate

class RosterEntryServiceTest {

    private val rosterEntryRepository = mock<RosterEntryRepository>()
    private val playerRepository = mock<PlayerRepository>()
    private val rosterEntryService = RosterEntryService(rosterEntryRepository, playerRepository)

    @Test
    fun `should retrieve paginated roster entries`() {
        val rosterEntries = listOf(
            RosterEntry(
                Player("jsmith001", "John", "Smith", "R", "R", LocalDate.of(1995, 1, 1)),
                Team("TEX", "Texas Rangers", "AL", 1971, 2024), 2020, "Pitcher"
            ),
            RosterEntry(
                Player("jdoe001", "Jane", "Doe", "L", "R", LocalDate.of(1996, 2, 2)),
                Team("NYM", "New York Mets", "NL", 1962, 2024), 2020, "Catcher"
            )
        )
        val pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "playerName")
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

        val result = rosterEntryService.getRosters("", 0, 10, "playerName", "asc")

        assertNotNull(result)
        assertEquals(2, result.content.size)
        assertEquals("Doe", result.content[0].player.lastName)
        assertEquals("Smith", result.content[1].player.lastName)
    }
}