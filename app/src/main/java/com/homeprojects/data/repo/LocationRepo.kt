package com.homeprojects.data.repo

import com.homeprojects.data.db.LocationDao
import com.homeprojects.data.mappers.LocationMapper
import com.homeprojects.data.mappers.ProjectMapper
import com.homeprojects.model.Location
import com.homeprojects.model.Project
import javax.inject.Inject

class LocationRepo @Inject constructor(val locationDao: LocationDao) {

    suspend fun addLocation(location: Location) {
        locationDao.insert(
            LocationMapper.toEntity(location)
        )
    }

    suspend fun updateLocation(location: Location) {
        locationDao.update(
            LocationMapper.toEntity(location)
        )
    }

    suspend fun deleteLocation(location: Location) {
        locationDao.delete(
            LocationMapper.toEntity(location)
        )
    }

    suspend fun getLocation(name: String): Location? {
        var location: Location? = null

        locationDao.getLocation(name)?.let {
            location = LocationMapper.fromEntity(it)
        }

        return location
    }

    suspend fun getAllLocations(): List<Location> {
        return locationDao.getAllLocations().map {
            LocationMapper.fromEntity(it)
        }
    }
}