package com.homeprojects.data.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.homeprojects.HomeProjectApp
import com.homeprojects.data.converters.ProjectCostConverter
import com.homeprojects.data.converters.ProjectPriorityConverter
import com.homeprojects.data.converters.ProjectSizeConverter
import com.homeprojects.data.converters.ProjectStatusConverter
import com.homeprojects.data.models.LocationEntity
import com.homeprojects.data.models.ProjectEntity

@Database(
    entities = [
        ProjectEntity::class,
        LocationEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    ProjectCostConverter::class,
    ProjectPriorityConverter::class,
    ProjectSizeConverter::class,
    ProjectStatusConverter::class
)
abstract class ProjectsDatabase: RoomDatabase() {

    abstract fun projectDao(): ProjectDao
    abstract fun locationDao(): LocationDao

    companion object {
        const val PROJECTS_TABLE = "projects"
        const val LOCATIONS_TABLE = "locations"

        private const val DATABASE_NAME = "home-project-db"

        @Volatile
        private var instance: ProjectsDatabase? = null

        fun getInstance(): ProjectsDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase().also { instance = it }
            }
        }

        private fun buildDatabase(): ProjectsDatabase {
            return Room.databaseBuilder(HomeProjectApp.context, ProjectsDatabase::class.java, DATABASE_NAME)
                .build()
        }
    }
}