package com.homeprojects.presentation.project

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.homeprojects.data.repo.LocationRepo
import com.homeprojects.data.repo.ProjectRepo
import com.homeprojects.model.Location
import com.homeprojects.model.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProjectViewModel @ViewModelInject constructor(
    val projectsRepo: ProjectRepo,
    val locationsRepo: LocationRepo
): ViewModel() {

    var projectId: String = ""
        set(newId) {
            field = newId

            project = projectsRepo.getProjectLiveData(newId)

            project!!.observeForever {
                // calls to project.value will be null if we don't observe the LiveData
            }

            viewModelScope.launch(Dispatchers.IO) {
                val projSnapshot = projectsRepo.getProject(newId)

                viewModelScope.launch(Dispatchers.Main) {
                    _projectInitialSnapshot.value = projSnapshot
                }
            }
        }

    private val _projectInitialSnapshot = MutableLiveData<Project>()
    val projectInitialSnapshot: LiveData<Project> = _projectInitialSnapshot

    var project: LiveData<Project>? = null

    val locations: LiveData<List<Location>> = locationsRepo.getLocations()


    fun addProject(project: Project) = viewModelScope.launch(Dispatchers.IO) {
            projectsRepo.addProject(project)
    }

    fun updateProject(project: Project) = viewModelScope.launch(Dispatchers.IO) {
            projectsRepo.updateProject(project)
    }

    fun deleteProject(project: Project) = viewModelScope.launch(Dispatchers.IO) {
            projectsRepo.deleteProject(project)
    }

    fun addLocation(location: Location) = viewModelScope.launch(Dispatchers.IO) {
        locationsRepo.addLocation(location)
    }
}