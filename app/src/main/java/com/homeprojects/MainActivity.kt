package com.homeprojects

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.ui.tooling.preview.Preview
import com.homeprojects.data.cache.Locations
import com.homeprojects.model.*
import com.homeprojects.presentation.ProjectListViewModel
import com.homeprojects.ui.HomeProjectsTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var viewModel: ProjectListViewModel

    private val showingNewProjectDialog = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectsListScreen(viewModel.projects)
        }
    }

    private fun showNewProjectDialog() {
        showingNewProjectDialog.value = true
    }

    @Composable
    fun NewProjectDialogContent(
            titleState: MutableState<TextFieldValue>,
            descState: MutableState<TextFieldValue>,
            locationState: MutableState<Location>,
            priorityState: MutableState<Float>,
            sizeState: MutableState<Float>,
            costState: MutableState<Float>
    ) {
        Column {
            TextField(
                    value = titleState.value,
                    onValueChange = { newVal -> titleState.value = newVal },
                    label = @Composable { Text("Title") }
            )

            Spacer(modifier = Modifier.padding(8.dp))

            TextField(
                    value = descState.value,
                    onValueChange = { newVal -> descState.value = newVal },
                    label = @Composable { Text("Description") }
            )

            Spacer(modifier = Modifier.padding(8.dp))

            Row(verticalGravity = Alignment.CenterVertically) {
                Text("Location", color = MaterialTheme.colors.primary)

                Spacer(modifier = Modifier.padding(8.dp))

                val locationExpanded = remember { mutableStateOf(false) }

                DropdownMenu(
                        toggle = {
                            Text(
                                text = locationState.value.name,
                                modifier = Modifier.padding(8.dp, 4.dp).clickable(
                                    onClick = {
                                        locationExpanded.value = true
                                    }
                                )
                            )
                        },
                        expanded = locationExpanded.value,
                        onDismissRequest = { locationExpanded.value = false }
                ) {
                    Locations.list().forEach { location ->
                        DropdownMenuItem(
                                onClick = {
                                    locationExpanded.value = false
                                    locationState.value = location
                                }
                        ) {
                            Text(location.name)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.padding(4.dp))

            Row(verticalGravity = Alignment.CenterVertically) {
                Text("Priority", color = MaterialTheme.colors.primary)

                Spacer(modifier = Modifier.padding(8.dp))

                Slider(
                        value = priorityState.value,
                        onValueChange = { priorityState.value = it },
                        valueRange = 0f..3f,
                        steps = 2
                )

            }

            Row(verticalGravity = Alignment.CenterVertically) {
                Text("Size", color = MaterialTheme.colors.primary)

                Spacer(modifier = Modifier.padding(8.dp))

                Slider(
                        value = sizeState.value,
                        onValueChange = { sizeState.value = it },
                        valueRange = 0f..3f,
                        steps = 2
                )
            }

            Row(verticalGravity = Alignment.CenterVertically) {
                Text("Cost", color = MaterialTheme.colors.primary)

                Spacer(modifier = Modifier.padding(8.dp))

                Slider(
                        value = costState.value,
                        onValueChange = { costState.value = it },
                        valueRange = 0f..3f,
                        steps = 2
                )
            }
        }
    }

    @Composable
    fun NewProjectDialog() {
        val titleState = remember { mutableStateOf(TextFieldValue("")) }
        val descState = remember { mutableStateOf(TextFieldValue("")) }
        val locationState = remember { mutableStateOf(Location.INTERIOR) }
        val priorityState = remember { mutableStateOf(0f) }
        val sizeState = remember { mutableStateOf(0f) }
        val costState = remember { mutableStateOf(0f) }

        AlertDialog(
                onDismissRequest = { showingNewProjectDialog.value = false },
                title = { Text(text = "Add a new project") },
                text = {
                    NewProjectDialogContent(
                            titleState = titleState,
                            descState = descState,
                            locationState = locationState,
                            priorityState = priorityState,
                            sizeState = sizeState,
                            costState = costState
                    )
                },
                confirmButton = {
                    Button(
                            onClick = {
                                viewModel.addProject(
                                        Project(
                                                title = titleState.value.text,
                                                description = descState.value.text,
                                                location = locationState.value,
                                                priority = Priority.values()[priorityState.value.toInt()],
                                                size = ProjectSize.values()[sizeState.value.toInt()],
                                                cost = Cost.values()[costState.value.toInt()]
                                        )
                                )
                                showingNewProjectDialog.value = false
                            }
                    ) {
                        Text(text = "Add")
                    }
                }
        )
    }

    @Composable
    fun AddProjectButton(modifier: Modifier = Modifier) {
        FloatingActionButton(modifier = modifier, onClick = { showNewProjectDialog() }) {
            Icon(asset = Icons.Filled.Add)
        }
    }

    @Composable
    fun ProjectListItem(project: Project) {
        val context: Context = ContextAmbient.current

        val defaultColor = MaterialTheme.colors.surface
        val pressedColor = MaterialTheme.colors.primary

        var bgColor: Color = defaultColor

        val priorityText = "!".repeat(project.priority.ordinal + 1)
        val sizeText = "+".repeat(project.size.ordinal + 1)
        val costText = "$".repeat(project.cost.ordinal + 1)

        Column(
                modifier = Modifier.fillMaxWidth()
                        .toggleable(
                                value = false,
                                onValueChange = { pressed ->
                                    bgColor = if (pressed) pressedColor else defaultColor
                                }
                        )
                        .clickable(
                                onClick = {
                                    Toast.makeText(context, "Clicked ${project.title}", Toast.LENGTH_SHORT).show()
                                }
                        )
        ) {
            Surface(
                    color = bgColor,
                    modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                        verticalGravity = Alignment.CenterVertically
                ) {
                    Text(
                            text = "[${project.location.name}]",
                            color = MaterialTheme.colors.primary,
                            style = TextStyle(
                                    fontFamily = FontFamily.Default,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp
                            ),
                            modifier = Modifier.padding(start = 16.dp, end = 8.dp)
                    )

                    Column(
                            modifier = Modifier.fillMaxWidth().padding(16.dp)
                    ) {
                        Text(
                                text = project.title,
                                color = MaterialTheme.colors.onSurface,
                                style = TextStyle(
                                        fontFamily = FontFamily.Default,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 16.sp
                                )
                        )
                        if (project.description.isNotEmpty()) {
                            Text(
                                    text = project.description,
                                    color = Color(0xFFAAAAAA),
                                    style = TextStyle(
                                            fontFamily = FontFamily.Default,
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 14.sp
                                    ),
                                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                            )
                        }
                        Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                        ) {
                            Text(
                                    text = priorityText,
                                    color = MaterialTheme.colors.onSurface
                            )

                            Spacer(modifier = Modifier.padding(horizontal = 8.dp))

                            Text(
                                    text = sizeText,
                                    color = MaterialTheme.colors.onSurface
                            )

                            Spacer(modifier = Modifier.padding(horizontal = 8.dp))

                            Text(
                                    text = costText,
                                    color = MaterialTheme.colors.onSurface
                            )
                        }
                    }
                }
            }
            Divider()
        }
    }

    @Composable
    fun ProjectsList(projectList: LiveData<List<Project>>) {
        val projects = projectList.observeAsState(emptyList())

        LazyColumnFor(items = projects.value) { project ->
            ProjectListItem(project)
        }
    }

    @Composable
    fun ProjectsListScreen(projectList: LiveData<List<Project>>) {
        HomeProjectsTheme {
            Stack(
                    modifier = Modifier.fillMaxSize()
            ) {
                val projects = projectList.observeAsState().value
                val hasProjects = projects?.isNotEmpty() ?: false

                if (hasProjects)
                    ProjectsList(projectList)
                else
                    Text(
                            text = "No projects added yet",
                            modifier = Modifier.gravity(Alignment.Center)
                    )

                AddProjectButton(
                        modifier = Modifier.gravity(Alignment.BottomEnd).padding(16.dp)
                )
                if (showingNewProjectDialog.value) {
                    NewProjectDialog()
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
//        HomeProjectsTheme {
//            Stack(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//                LazyColumnFor(
//                        items = arrayListOf(
//                                Project(title = "First"),
//                                Project(title = "Second"),
//                                Project(title = "Third"),
//                                Project(title = "Fourth"),
//                                Project(title = "Fifth")
//                        )
//                ) { project ->
//                    ProjectListItem(project = project)
//                }
//                AddProjectButton(modifier = Modifier.gravity(Alignment.BottomEnd))
//            }
//        }
    }
}