package com.homeprojects.di

import com.homeprojects.data.db.LocationDao
import com.homeprojects.data.db.ProjectDao
import com.homeprojects.data.db.ProjectsDatabase
import com.homeprojects.data.repo.LocationRepo
import com.homeprojects.data.repo.ProjectRepo
import com.homeprojects.presentation.locations.LocationsViewModel
import com.homeprojects.presentation.projects.ProjectsViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideProjectDatabase() = ProjectsDatabase.getInstance()

    @Singleton
    @Provides
    fun provideProjectDao(db: ProjectsDatabase) = db.projectDao()

    @Singleton
    @Provides
    fun provideProjectRepo(projectDao: ProjectDao) = ProjectRepo(projectDao)

    @Singleton
    @Provides
    fun provideLocationDao(db: ProjectsDatabase) = db.locationDao()

    @Singleton
    @Provides
    fun provideLocationRepo(locationDao: LocationDao) = LocationRepo(locationDao)

    @Provides
    fun provideProjectsViewModel(projectsRepo: ProjectRepo, locationRepo: LocationRepo) = ProjectsViewModel(projectsRepo, locationRepo)

    @Provides
    fun provideLocationsViewModel(projectsRepo: ProjectRepo, locationRepo: LocationRepo) = LocationsViewModel(projectsRepo, locationRepo)

}