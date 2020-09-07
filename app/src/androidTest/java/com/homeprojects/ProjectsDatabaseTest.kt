package com.homeprojects

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.homeprojects.data.db.ProjectsDatabase
import com.homeprojects.data.models.LocationEntity
import com.homeprojects.data.models.ProjectEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
open class ProjectsDatabaseTest {

    private lateinit var projectsDb: ProjectsDatabase

    @Before
    fun initDb() {
        projectsDb = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            ProjectsDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() {
        projectsDb.close()
    }

    @Test
    fun testInsertProject() = runBlocking {
        val fakeProjectEntity = ProjectEntity()
        projectsDb.projectDao().insert(fakeProjectEntity)

        val projects = projectsDb.projectDao().getAllProjects()
        assert(projects.isNotEmpty())
    }

    @Test
    fun testGetProject() = runBlocking {
        val id = UUID.randomUUID().toString()
        val title = "My New Project"

        val fakeProjectEntity = ProjectEntity(id = id, title = title)
        projectsDb.projectDao().insert(fakeProjectEntity)

        val project = projectsDb.projectDao().getProject(id)
        assertEquals(title, project!!.title)
    }

    @Test
    fun testInsertLocation() = runBlocking {
        val fakeLocationEntity = LocationEntity("Bridge to Nowhere")
        projectsDb.locationDao().insert(fakeLocationEntity)

        val locations = projectsDb.locationDao().getAllLocations()
        assert(locations.isNotEmpty())
    }
}