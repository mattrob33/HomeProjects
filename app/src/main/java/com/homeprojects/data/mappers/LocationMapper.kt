package com.homeprojects.data.mappers

import com.homeprojects.data.models.LocationEntity
import com.homeprojects.model.*

class LocationMapper {
    companion object {
        fun fromEntity(entity: LocationEntity): Location {
            return Location(
                name = entity.name
            )
        }

        fun toEntity(location: Location): LocationEntity {
            return LocationEntity(
                name = location.name
            )
        }
    }
}