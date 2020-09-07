package com.homeprojects.data.converters

import androidx.room.TypeConverter
import com.homeprojects.model.Priority

class ProjectPriorityConverter {
    @TypeConverter
    fun projectPriorityToString(priority: Priority): String {
        return priority.name
    }

    @TypeConverter
    fun fromString(value: String): Priority {
        return Priority.valueOf(value)
    }
}