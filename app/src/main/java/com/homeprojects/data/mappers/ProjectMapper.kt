package com.homeprojects.data.mappers

import com.homeprojects.data.cache.Locations
import com.homeprojects.data.models.ProjectEntity
import com.homeprojects.model.Project

class ProjectMapper {
    companion object {
        fun fromEntity(entity: ProjectEntity): Project {
            return Project(
                id = entity.id,
                title = entity.title,
                description = entity.description,
                status = entity.status,
                priority = entity.priority,
                size = entity.size,
                cost = entity.cost,
                location = Locations.from(entity.location)
            )
        }

        fun toEntity(project: Project): ProjectEntity {
            return ProjectEntity(
                id = project.id,
                title = project.title,
                description = project.description,
                status = project.status,
                priority = project.priority,
                size = project.size,
                cost = project.cost,
                location = project.location.name
            )
        }
    }
}