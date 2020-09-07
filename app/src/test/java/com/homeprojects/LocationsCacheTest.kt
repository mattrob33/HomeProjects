package com.homeprojects

import com.homeprojects.data.cache.Locations
import com.homeprojects.model.Location
import org.junit.Assert.assertEquals
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import java.util.*

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class LocationsCacheTest {
    @Test
    fun `Locations cache initial size is correct`() {
        assertEquals(4, Locations.size())
    }

    @Test
    fun `When existing location is requested, size does not change`() {
        val startSize = Locations.size()
        Locations.from(Location.INTERIOR.name)
        assertEquals(startSize, Locations.size())
    }

    @Test
    fun `When existing location is requested, returned location is correct`() {
        val location = Locations.from(Location.INTERIOR.name)
        assertEquals(location, Location.INTERIOR)
    }

    @Test
    fun `When new location is requested, size increases`() {
        val startSize = Locations.size()
        Locations.from("NEW ROOM")
        assertEquals(startSize + 1, Locations.size())
    }

    @Test
    fun `When new location is requested, returned location is correct`() {
        val newName = UUID.randomUUID().toString()
        val location = Locations.from(newName)
        assertEquals(newName, location.name)
    }
}