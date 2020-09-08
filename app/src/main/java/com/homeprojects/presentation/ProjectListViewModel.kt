package com.homeprojects.presentation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.homeprojects.data.repo.ProjectRepo
import com.homeprojects.model.Project
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProjectListViewModel @ViewModelInject constructor(
    val projectsRepo: ProjectRepo
): ViewModel() {
    val projects = projectsRepo.getProjectsFlow()

    fun addProject(project: Project) {
        CoroutineScope(Dispatchers.IO).launch {
            projectsRepo.addProject(project)
        }
    }
}