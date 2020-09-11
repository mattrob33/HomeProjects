package com.homeprojects.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.homeprojects.data.db.ProjectsDatabase.Companion.PROJECTS_TABLE
import com.homeprojects.model.*
import java.util.*

@Entity(tableName = PROJECTS_TABLE)
data class ProjectEntity(
        @PrimaryKey val id: String = UUID.randomUUID().toString(),
        var title: String = "",
        var description: String = "",
        var status: ProjectStatus = ProjectStatus.INACTIVE,
        var priority: Priority = Priority.NONE,
        var size: ProjectSize = ProjectSize.NONE,
        var cost: Cost = Cost.FREE,
        var location: String = ""
)