package com.homeprojects.data.converters

import androidx.room.TypeConverter
import com.homeprojects.model.Cost

class ProjectCostConverter {
    @TypeConverter
    fun projectCostToString(cost: Cost): String {
        return cost.name
    }

    @TypeConverter
    fun fromString(value: String): Cost {
        return Cost.valueOf(value)
    }
}