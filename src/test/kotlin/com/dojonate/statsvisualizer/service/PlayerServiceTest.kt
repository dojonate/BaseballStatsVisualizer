package com.dojonate.statsvisualizer.service

import com.dojonate.statsvisualizer.model.Player
import com.dojonate.statsvisualizer.repository.PlayerRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.util.Optional

class PlayerServiceTest {

    private val playerRepository = mock<PlayerRepository>() // Mock the repository
    private val playerService = PlayerService(playerRepository) // Inject the mock

    @Test
    fun `should save a new player`() {
        val player = Player("p001", "John", "Doe", "R", "L", null)

        whenever(playerRepository.findById(player.playerId)).thenReturn(Optional.empty())
        whenever(playerRepository.save(player)).thenReturn(player)

        val savedPlayer = playerService.save(player)

        assertNotNull(savedPlayer)
        assertEquals("p001", savedPlayer.playerId)
        verify(playerRepository, times(1)).save(player)
    }

    @Test
    fun `should not save an existing player`() {
        val player = Player("p001", "John", "Doe", "R", "L", null)

        whenever(playerRepository.findById(player.playerId)).thenReturn(Optional.of(player))

        val savedPlayer = playerService.save(player)

        assertNotNull(savedPlayer)
        assertEquals("p001", savedPlayer.playerId)
        verify(playerRepository, times(0)).save(any())
    }

    @Test
    fun `should find a player by ID`() {
        val player = Player("p001", "John", "Doe", "R", "L", null)

        whenever(playerRepository.findById("p001")).thenReturn(Optional.of(player))

        val foundPlayer = playerService.findById("p001")

        assertNotNull(foundPlayer)
        assertEquals("p001", foundPlayer?.playerId)
        assertEquals("John", foundPlayer?.firstName)
    }

    @Test
    fun `should return null when player not found`() {
        whenever(playerRepository.findById("p002")).thenReturn(Optional.empty())

        val foundPlayer = playerService.findById("p002")

        assertNull(foundPlayer)
    }

    @Test
    fun `should retrieve all players`() {
        val players = listOf(
            Player("p001", "John", "Doe", "R", "L", null),
            Player("p002", "Jane", "Smith", "L", "R", null)
        )

        whenever(playerRepository.findAll()).thenReturn(players)

        val foundPlayers = playerService.findAll()

        assertNotNull(foundPlayers)
        assertEquals(2, foundPlayers.size)
        assertEquals("p001", foundPlayers[0].playerId)
        assertEquals("p002", foundPlayers[1].playerId)
    }
}
