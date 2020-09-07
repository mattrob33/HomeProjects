package com.homeprojects.data.converters

import androidx.room.TypeConverter
import com.homeprojects.model.ProjectSize
import com.homeprojects.model.ProjectStatus

class ProjectSizeConverter {
    @TypeConverter
    fun projectSizeToString(size: ProjectSize): String {
        return size.name
    }

    @TypeConverter
    fun fromString(value: String): ProjectSize {
        return ProjectSize.valueOf(value)
    }
}