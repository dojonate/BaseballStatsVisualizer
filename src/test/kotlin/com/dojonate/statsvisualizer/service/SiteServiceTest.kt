package com.dojonate.statsvisualizer.service

import com.dojonate.statsvisualizer.model.Site
import com.dojonate.statsvisualizer.repository.SiteRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class SiteServiceTest {

    private val siteRepository: SiteRepository = mock()
    private val siteService = SiteService(siteRepository)

    @Test
    fun `should save a new site`() {
        val site = Site().apply {
            // Assuming we can set parkId and name (e.g. via reflection or if they were vars)
            // For our test, we assume the fields are accessible:
            (this::class.java.getDeclaredField("parkId")).apply { isAccessible = true }.set(this, "SITE123")
            name = "Test Park"
        }
        whenever(siteRepository.findById("SITE123")).thenReturn(java.util.Optional.empty())
        whenever(siteRepository.save(site)).thenReturn(site)

        val savedSite = siteService.save(site)
        assertNotNull(savedSite)
        assertEquals("SITE123", savedSite.siteId)
        verify(siteRepository, times(1)).save(site)
    }

    @Test
    fun `should find site by id`() {
        val site = Site().apply {
            (this::class.java.getDeclaredField("parkId")).apply { isAccessible = true }.set(this, "SITE123")
            name = "Test Park"
        }
        whenever(siteRepository.findById("SITE123")).thenReturn(java.util.Optional.of(site))

        val foundSite = siteService.findById("SITE123")
        assertNotNull(foundSite)
        assertEquals("SITE123", foundSite?.siteId)
    }

    @Test
    fun `should retrieve all sites`() {
        val site1 = Site().apply {
            (this::class.java.getDeclaredField("parkId")).apply { isAccessible = true }.set(this, "SITE1")
            name = "Park One"
        }
        val site2 = Site().apply {
            (this::class.java.getDeclaredField("parkId")).apply { isAccessible = true }.set(this, "SITE2")
            name = "Park Two"
        }
        whenever(siteRepository.findAll()).thenReturn(listOf(site1, site2))

        val sites = siteService.findAll()
        assertEquals(2, sites.size)
    }
}
