package com.dojonate.statsvisualizer.service

import com.dojonate.statsvisualizer.model.Game
import com.dojonate.statsvisualizer.repository.GameRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GameServiceTest {

    private val gameRepository: GameRepository = mock()
    private val gameService = GameService(gameRepository)

    @Test
    fun `should save a game`() {
        val game = Game().apply { id = "GAME123" }
        whenever(gameRepository.save(game)).thenReturn(game)

        val savedGame = gameService.save(game)

        assertNotNull(savedGame)
        assertEquals("GAME123", savedGame.id)
        verify(gameRepository, times(1)).save(game)
    }

    @Test
    fun `should find game by id`() {
        val game = Game().apply { id = "GAME123" }
        whenever(gameRepository.findById("GAME123")).thenReturn(java.util.Optional.of(game))

        val foundGame = gameService.findById("GAME123")
        assertNotNull(foundGame)
        assertEquals("GAME123", foundGame.get().id)
    }

    @Test
    fun `should return null when game not found`() {
        whenever(gameRepository.findById("GAME999")).thenReturn(java.util.Optional.empty())
        val foundGame = gameService.findById("GAME999")
        assertEquals(foundGame.isEmpty, true)
    }

    @Test
    fun `should delete game by id`() {
        gameService.deleteById("GAME123")
        verify(gameRepository, times(1)).deleteById("GAME123")
    }

    @Test
    fun `should retrieve all games`() {
        val games = listOf(Game().apply { id = "GAME1" }, Game().apply { id = "GAME2" })
        whenever(gameRepository.findAll()).thenReturn(games)

        val allGames = gameService.findAll()
        assertEquals(2, allGames.size)
    }
}
