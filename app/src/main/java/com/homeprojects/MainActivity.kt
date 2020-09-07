package com.homeprojects

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.ui.tooling.preview.Preview
import com.homeprojects.data.repo.ProjectRepo
import com.homeprojects.model.Project
import com.homeprojects.ui.HomeProjectsTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var projectsRepo: ProjectRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectsListScreen(
                projectsRepo.getProjectsFlow()
            )
        }
    }

    @Composable
    fun Greeting(name: String) {
        Text(text = "Hello $name!")
    }

    @Composable
    fun AddProjectButton(modifier: Modifier = Modifier) {
        FloatingActionButton(modifier = modifier, onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                projectsRepo.addProject(
                    Project(
                        title = UUID.randomUUID().toString()
                    )
                )
            }
        }) {
            Icon(asset = Icons.Filled.Add)
        }
    }

    @Composable
    fun ProjectsList(projectList: LiveData<List<Project>>) {
        val projects = projectList.observeAsState(emptyList())
        LazyColumnFor(items = projects.value) { project ->
            Text(text = project.title)
        }
    }

    @Composable
    fun ProjectsListScreen(projectList: LiveData<List<Project>>) {
        HomeProjectsTheme {
            Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                Greeting("Android")
                ProjectsList(projectList)
                AddProjectButton(modifier = Modifier.gravity(Alignment.End))
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
//    ProjectsListScreen()
    }
}