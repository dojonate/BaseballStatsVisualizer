package com.dojonate.statsvisualizer.service

import com.dojonate.statsvisualizer.model.PlayerEvent
import com.dojonate.statsvisualizer.repository.PlayerEventRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class PlayerEventServiceTest {

    private val playerEventRepository: PlayerEventRepository = mock()
    private val playerEventService = PlayerEventService(playerEventRepository)

    @Test
    fun `should save a player event`() {
        val event = PlayerEvent().apply { setCount(1, 2) }
        whenever(playerEventRepository.save(event)).thenReturn(event)

        val savedEvent = playerEventService.save(event)
        assertNotNull(savedEvent)
        assertArrayEquals(intArrayOf(1, 2), savedEvent.count)
        verify(playerEventRepository, times(1)).save(event)
    }

    @Test
    fun `should save all player events`() {
        val event1 = PlayerEvent().apply { setCount(1, 1) }
        val event2 = PlayerEvent().apply { setCount(2, 2) }
        val events = listOf(event1, event2)
        whenever(playerEventRepository.saveAll(events)).thenReturn(events)

        val savedEvents = playerEventService.saveAll(events)
        assertEquals(2, savedEvents.size)
        verify(playerEventRepository, times(1)).saveAll(events)
    }

    @Test
    fun `should find all player events`() {
        val event1 = PlayerEvent().apply { setCount(1, 1) }
        val event2 = PlayerEvent().apply { setCount(2, 2) }
        whenever(playerEventRepository.findAll()).thenReturn(listOf(event1, event2))

        val events = playerEventService.findAll()
        assertEquals(2, events.size)
    }

    @Test
    fun `should find player event by id`() {
        val event = PlayerEvent().apply { setCount(3, 3) }
        event.id = 100L
        whenever(playerEventRepository.findById(100L)).thenReturn(java.util.Optional.of(event))

        val foundEvent = playerEventService.findById(100L)
        assertNotNull(foundEvent)
        assertEquals(100L, foundEvent.get().id)
    }

    @Test
    fun `should delete player event by id`() {
        playerEventService.deleteById(200L)
        verify(playerEventRepository, times(1)).deleteById(200L)
    }
}
