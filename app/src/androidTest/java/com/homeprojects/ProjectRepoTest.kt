package com.homeprojects

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.homeprojects.data.db.ProjectsDatabase
import com.homeprojects.data.repo.ProjectRepo
import com.homeprojects.model.Project
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
open class ProjectRepoTest {

    private lateinit var projectsDb: ProjectsDatabase
    private lateinit var projectRepo: ProjectRepo

    @Before
    fun initDb() {
        projectsDb = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            ProjectsDatabase::class.java
        ).build()

        projectRepo = ProjectRepo(projectsDb.projectDao())
    }

    @After
    fun closeDb() {
        projectsDb.close()
    }

    @Test
    fun testAddProject() = runBlocking {
        val fakeProject = Project()
        projectRepo.addProject(fakeProject)

        val projects = projectRepo.getAllProjects()
        assert(projects.isNotEmpty())
    }

    @Test
    fun testUpdateProject() = runBlocking {
        val id = UUID.randomUUID().toString()
        val oldTitle = UUID.randomUUID().toString()
        val newTitle = UUID.randomUUID().toString()

        val fakeProject = Project(id = id, title = oldTitle)
        projectRepo.addProject(fakeProject)

        fakeProject.title = newTitle
        projectRepo.updateProject(fakeProject)

        val project = projectRepo.getProject(id)!!
        assertEquals(newTitle, project.title)
    }

    @Test
    fun testDeleteProject() = runBlocking {
        val id = UUID.randomUUID().toString()
        val title = UUID.randomUUID().toString()

        val fakeProject = Project(id = id, title = title)
        projectRepo.addProject(fakeProject)
        assertNotNull(projectRepo.getProject(id))

        projectRepo.deleteProject(fakeProject)
        assertNull(projectRepo.getProject(id))
    }

    @Test
    fun testGetProject() = runBlocking {
        val id = UUID.randomUUID().toString()
        val title = "My New Project"

        val fakeProject = Project(id = id, title = title)
        projectRepo.addProject(fakeProject)

        val project = projectRepo.getProject(id)
        assertEquals(title, project!!.title)
    }
}