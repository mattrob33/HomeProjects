package com.homeprojects.di

import com.homeprojects.data.db.ProjectDao
import com.homeprojects.data.db.ProjectsDatabase
import com.homeprojects.data.repo.ProjectRepo
import com.homeprojects.presentation.ProjectListViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object AppModule {

    @Provides
    fun provideProjectDatabase() = ProjectsDatabase.getInstance()

    @Provides
    fun provideProjectDao(db: ProjectsDatabase) = db.projectDao()

    @Provides
    fun provideProjectRepo(projectDao: ProjectDao) = ProjectRepo(projectDao)

    @Provides
    fun provideProjectListViewModel(projectsRepo: ProjectRepo) = ProjectListViewModel(projectsRepo)

}