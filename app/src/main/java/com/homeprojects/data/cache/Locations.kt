package com.homeprojects.data.cache

import com.homeprojects.model.Location

object Locations {
    private val locations: ArrayList<Location> = arrayListOf(
        Location.INTERIOR,
        Location.EXTERIOR,
        Location.YARD,
        Location.SHOP
    )

    fun from(name: String): Location {
        val existing = locations.find { it.name == name }

        return if (existing == null) {
            val newLocation = Location(name)
            locations.add(newLocation)
            newLocation
        }
        else {
            existing
        }
    }

    fun size() = locations.size
}