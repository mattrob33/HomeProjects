package com.homeprojects.presentation.locations

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

class LocationsViewModel @ViewModelInject constructor(
        val projectsRepo: ProjectRepo,
        val locationsRepo: LocationRepo
): ViewModel() {

    val currentScreen = MutableLiveData(LocationScreen.LOCATIONS)

    val locations: LiveData<List<Location>> = locationsRepo.getLocations()

    var projectsForLocation: LiveData<List<Project>>? = null

    var selectedLocation: Location = Location.INTERIOR
        set(value) {
            field = value

            projectsForLocation = projectsRepo.getProjectsForLocation(value)
            projectsForLocation!!.observeForever {
                // to ensure it emits
            }
        }

    fun deleteLocation(location: Location) = viewModelScope.launch(Dispatchers.IO) {
        locationsRepo.deleteLocation(location)
    }

    fun deleteProject(project: Project) = viewModelScope.launch(Dispatchers.IO) {
        projectsRepo.deleteProject(project)
    }
}

enum class LocationScreen {
    LOCATIONS,
    PROJECTS_FOR_LOCATION
}