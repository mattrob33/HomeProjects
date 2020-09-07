package com.homeprojects.data.db

import androidx.room.*
import com.homeprojects.data.db.ProjectsDatabase.Companion.LOCATIONS_TABLE
import com.homeprojects.data.models.LocationEntity

@Dao
interface LocationDao {
    @Insert
    suspend fun insert(project: LocationEntity)

    @Update
    suspend fun update(vararg project: LocationEntity)

    @Delete
    suspend fun delete(vararg project: LocationEntity)

    @Query("SELECT * FROM $LOCATIONS_TABLE WHERE `name` = :name LIMIT 1")
    suspend fun getLocation(name: String): LocationEntity?

    @Query("SELECT * FROM $LOCATIONS_TABLE")
    suspend fun getAllLocations(): List<LocationEntity>
}