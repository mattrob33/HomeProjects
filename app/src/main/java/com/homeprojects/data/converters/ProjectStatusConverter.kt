package com.homeprojects.data.converters

import androidx.room.TypeConverter
import com.homeprojects.model.ProjectStatus

class ProjectStatusConverter {
    @TypeConverter
    fun projectStatusToString(status: ProjectStatus): String {
        return status.name
    }

    @TypeConverter
    fun fromString(value: String): ProjectStatus {
        return ProjectStatus.valueOf(value)
    }
}