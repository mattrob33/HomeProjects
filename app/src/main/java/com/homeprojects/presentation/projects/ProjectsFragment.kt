package com.homeprojects.presentation.projects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.ui.tooling.preview.Preview
import com.homeprojects.model.*
import com.homeprojects.ui.HomeProjectsTheme
import com.homeprojects.ui.darkSurface
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProjectsFragment : Fragment() {

    @Inject lateinit var viewModel: ProjectsViewModel

    private var showProjectDialog by mutableStateOf(false)
    private var showLocationDialog by mutableStateOf(false)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                ProjectsScreen(viewModel.projects)
            }
        }
    }

    private fun showNewLocationDialog() {
        showLocationDialog = true
    }

    @Composable
    fun NewLocationDialog() {
        val titleState = remember { mutableStateOf(TextFieldValue("")) }

        AlertDialog(
                onDismissRequest = { showLocationDialog = false },
                title = { Text("New Location") },
                text = {
                    TextField(
                            value = titleState.value,
                            onValueChange = { newVal -> titleState.value = newVal },
                            label = @Composable { Text("Location Name") }
                    )
                },
                confirmButton = {
                    Button(
                            onClick = {
                                viewModel.addLocation(
                                        Location(
                                                name = titleState.value.text
                                        )
                                )
                                showLocationDialog = false
                            }
                    ) {
                        Text(text = "Add")
                    }
                }
        )
    }

    private fun showNewProjectDialog() {
        showProjectDialog = true
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
                    val locations = viewModel.locations.observeAsState()

                    locations.value?.sortedBy { it.name }?.forEach { location ->
                        DropdownMenuItem(
                                onClick = {
                                    locationExpanded.value = false
                                    locationState.value = location
                                }
                        ) {
                            Text(location.name)
                        }
                    }
                    DropdownMenuItem(
                            onClick = {
                                showNewLocationDialog()
                            }
                    ) {
                        Row(verticalGravity = Alignment.CenterVertically) {
                            Image(
                                    asset = Icons.Rounded.Add,
                                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface)
                            )
                            Spacer(modifier = Modifier.padding(end = 8.dp))
                            Text("New")
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
                onDismissRequest = { showProjectDialog = false },
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
                                showProjectDialog = false
                            }
                    ) {
                        Text(text = "Add")
                    }
                }
        )
    }

    @Composable
    fun AddProjectButton(modifier: Modifier = Modifier) {
        FloatingActionButton(
            modifier = modifier,
            onClick = { showNewProjectDialog() },
            backgroundColor = MaterialTheme.colors.primary
        ) {
            Icon(
                asset = Icons.Rounded.Add,
                tint = darkSurface
            )
        }
    }

    @Composable
    fun ProjectListItem(project: Project) {
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

//                                    viewModel.updateProject(
//                                            project.copy(
//                                                    status = when(project.status) {
//                                                        ProjectStatus.INACTIVE -> ProjectStatus.ACTIVE
//                                                        ProjectStatus.ACTIVE -> ProjectStatus.COMPLETED
//                                                        ProjectStatus.COMPLETED -> ProjectStatus.INACTIVE
//                                                    }
//                                            )
//                                    )

                                    val direction = ProjectsFragmentDirections.editProject(project.id)
                                    findNavController().navigate(direction)
                                },
                                onLongClick = {
                                    viewModel.deleteProject(project)
                                }
                        )
        ) {
            Surface(
                    color = bgColor,
                    modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Text(
                            text = project.title,
                            color = MaterialTheme.colors.onSurface,
                            style = TextStyle(
                                    fontFamily = FontFamily.Default,
                                    fontWeight = FontWeight.Bold,
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
                    Text(
                            text = "[${project.location.name}]",
                            color = MaterialTheme.colors.primary,
                            style = TextStyle(
                                    fontFamily = FontFamily.Default,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp
                            ),
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )
                }
            }
            Divider()
        }
    }

    @Composable
    fun ListSectionHeader(text: String) {
        Surface(
            color = MaterialTheme.colors.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(top = 24.dp)) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onSurface,
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                )
                Divider()
            }
        }
    }

    @Composable
    fun ProjectsList(projectList: LiveData<List<Project>>) {
        val projects by projectList.observeAsState(emptyList())

        val activeProjects = projects.filter { project -> project.status == ProjectStatus.ACTIVE }
        val inactiveProjects = projects.filter { project -> project.status == ProjectStatus.INACTIVE }
        val completedProjects = projects.filter { project -> project.status == ProjectStatus.COMPLETED }

        ScrollableColumn {
            ListSectionHeader(text = "Current")
            activeProjects.filter { project -> project.status == ProjectStatus.ACTIVE }.forEach { ProjectListItem(it) }

            ListSectionHeader(text = "Future")
            inactiveProjects.filter { project -> project.status == ProjectStatus.INACTIVE }.forEach { ProjectListItem(it) }

            ListSectionHeader(text = "Completed")
            completedProjects.filter { project -> project.status == ProjectStatus.COMPLETED }.forEach { ProjectListItem(it) }
        }
    }

    @Composable
    fun Dialogs() {
        if (showProjectDialog) NewProjectDialog()
        if (showLocationDialog) NewLocationDialog()
    }

    @Composable
    fun ProjectsScreen(projectList: LiveData<List<Project>>) {
        HomeProjectsTheme {
            Stack(
                    modifier = Modifier.fillMaxSize()
            ) {
                ProjectsList(projectList)

                AddProjectButton(modifier = Modifier.gravity(Alignment.BottomEnd).padding(16.dp))

                Dialogs()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
    }
}