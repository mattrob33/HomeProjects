package com.homeprojects.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.homeprojects.data.db.ProjectDao
import com.homeprojects.data.mappers.ProjectMapper
import com.homeprojects.model.Location
import com.homeprojects.model.Project
import javax.inject.Inject

class ProjectRepo @Inject constructor(val projectDao: ProjectDao) {

    suspend fun addProject(project: Project) {
        projectDao.insert(
            ProjectMapper.toEntity(project)
        )
    }

    suspend fun updateProject(project: Project) {
        projectDao.update(
            ProjectMapper.toEntity(project)
        )
    }

    suspend fun deleteProject(project: Project) {
        projectDao.delete(
            ProjectMapper.toEntity(project)
        )
    }

    suspend fun getProject(id: String): Project? {
        var project: Project? = null

        projectDao.getProject(id)?.let {
            project = ProjectMapper.fromEntity(it)
            // TODO: get tasks, resources, tags from their DAOs
        }

        return project
    }

    suspend fun getAllProjects(): List<Project> {
        return projectDao.getAllProjects().map {
            ProjectMapper.fromEntity(it)
        }
    }

    fun getProjectLiveData(id: String): LiveData<Project?> {
        return Transformations.map(projectDao.getProjectLiveData(id)) { entity ->
            if (entity == null)
                null
            else
                ProjectMapper.fromEntity(entity)
        }
    }

    fun getProjects(): LiveData<List<Project>> = Transformations.map(projectDao.getProjectsFlow()) { entityList ->
        entityList.map { entity ->
            ProjectMapper.fromEntity(entity)
        }
    }

    fun getProjectsForLocation(location: Location): LiveData<List<Project>> = Transformations.map(projectDao.getProjectsFlow()) { entityList ->
        entityList.map { entity ->
            ProjectMapper.fromEntity(entity)
        }.filter { project ->
            project.location == location
        }
    }
}