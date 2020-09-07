package com.homeprojects.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.homeprojects.data.db.ProjectsDatabase.Companion.LOCATIONS_TABLE

@Entity(tableName = LOCATIONS_TABLE)
data class LocationEntity(
    @PrimaryKey val name: String
    // TODO: add icon, bg color, etc
)