package com.homeprojects.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.homeprojects.data.db.ProjectsDatabase.Companion.PROJECTS_TABLE
import com.homeprojects.data.models.ProjectEntity

@Dao
interface ProjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(project: ProjectEntity)

    @Update
    suspend fun update(vararg project: ProjectEntity)

    @Delete
    suspend fun delete(vararg project: ProjectEntity)

    @Query("SELECT * FROM $PROJECTS_TABLE WHERE `id` = :id LIMIT 1")
    suspend fun getProject(id: String): ProjectEntity?

    @Query("SELECT * FROM $PROJECTS_TABLE")
    suspend fun getAllProjects(): List<ProjectEntity>

    @Query("SELECT * FROM $PROJECTS_TABLE")
    fun getProjectsFlow(): LiveData<List<ProjectEntity>>
}