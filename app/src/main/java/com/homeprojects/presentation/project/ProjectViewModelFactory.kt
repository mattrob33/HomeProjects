package com.homeprojects.presentation.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.homeprojects.data.repo.LocationRepo
import com.homeprojects.data.repo.ProjectRepo

@Suppress("UNCHECKED_CAST")
class ProjectViewModelFactory (
        val projectsRepo: ProjectRepo,
        val locationsRepo: LocationRepo
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProjectViewModel(projectsRepo = projectsRepo,
                locationsRepo = locationsRepo) as T
    }

}