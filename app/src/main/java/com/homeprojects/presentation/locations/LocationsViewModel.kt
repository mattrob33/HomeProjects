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

    val locations: LiveData<List<Location>> = locationsRepo.getLocations()

    fun deleteLocation(location: Location) = viewModelScope.launch(Dispatchers.IO) {
        locationsRepo.deleteLocation(location)
    }
}