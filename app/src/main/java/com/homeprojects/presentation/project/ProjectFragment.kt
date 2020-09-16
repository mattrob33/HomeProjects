package com.homeprojects.presentation.project

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.ui.tooling.preview.Preview
import com.homeprojects.data.repo.LocationRepo
import com.homeprojects.data.repo.ProjectRepo
import com.homeprojects.model.*
import com.homeprojects.ui.HomeProjectsTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProjectFragment : Fragment() {

    @Inject lateinit var projectRepo: ProjectRepo
    @Inject lateinit var locationRepo: LocationRepo

    private val viewModelFactory: ProjectViewModelFactory by lazy {
        ProjectViewModelFactory(projectRepo, locationRepo)
    }

    private val viewModel: ProjectViewModel by viewModels { viewModelFactory }

    private var showLocationDialog by mutableStateOf(false)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                viewModel.projectInitialSnapshot.observeAsState().value.let { project ->
                    if (project == null)
                        ProjectScreen( Project() )
                    else
                        ProjectScreen(project)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: ProjectFragmentArgs by navArgs()
        viewModel.projectId = args.projectId
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

    @Composable
    fun ProjectInfo(project: Project) {
        val titleState = remember { mutableStateOf(TextFieldValue(project.title)) }
        val descState = remember { mutableStateOf(TextFieldValue(project.description)) }
        val statusState: MutableState<ProjectStatus> = remember { mutableStateOf(project.status) }
        val locationState: MutableState<Location> = remember { mutableStateOf(project.location) }
        val priorityState: MutableState<Float> = remember { mutableStateOf(project.priority.ordinal.toFloat()) }
        val sizeState: MutableState<Float> = remember { mutableStateOf(project.size.ordinal.toFloat()) }
        val costState: MutableState<Float> = remember { mutableStateOf(project.cost.ordinal.toFloat()) }

        Column {
            TextField(
                    value = titleState.value,
                    onValueChange = { title ->
                        titleState.value = title
                        viewModel.project?.value?.let { proj ->
                            viewModel.updateProject(
                                proj.copy(title = title.text)
                            )
                        }
                    },
                    label = @Composable { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(16.dp))

            TextField(
                    value = descState.value,
                    onValueChange = { description ->
                        descState.value = description
                        viewModel.project?.value?.let { proj ->
                            viewModel.updateProject(
                                proj.copy(description = description.text)
                            )
                        }
                    },
                    label = @Composable { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(16.dp))

            Row(verticalGravity = Alignment.CenterVertically) {
                Text("Status", color = MaterialTheme.colors.primary)

                Spacer(modifier = Modifier.padding(16.dp))

                val statusExpanded = remember { mutableStateOf(false) }

                DropdownMenu(
                        toggle = {
                            Text(
                                    text = ProjectStatus.getName(statusState.value),
                                    color = MaterialTheme.colors.onSurface,
                                    modifier = Modifier.padding(8.dp, 4.dp).clickable(
                                            onClick = {
                                                statusExpanded.value = true
                                            }
                                    )
                            )
                        },
                        expanded = statusExpanded.value,
                        onDismissRequest = { statusExpanded.value = false }
                ) {
                    ProjectStatus.values().forEach { status ->
                        DropdownMenuItem(
                                onClick = {
                                    statusExpanded.value = false
                                    statusState.value = status

                                    viewModel.project?.value?.let { proj ->
                                        viewModel.updateProject(
                                            proj.copy(status = status)
                                        )
                                    }
                                }
                        ) {
                            Text(ProjectStatus.getName(status))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.padding(8.dp))

            Row(verticalGravity = Alignment.CenterVertically) {
                Text("Location", color = MaterialTheme.colors.primary)

                Spacer(modifier = Modifier.padding(16.dp))

                val locationExpanded = remember { mutableStateOf(false) }

                DropdownMenu(
                        toggle = {
                            Text(
                                    text = locationState.value.name,
                                    color = MaterialTheme.colors.onSurface,
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

                                    viewModel.project?.value?.let { proj ->
                                        viewModel.updateProject(
                                            proj.copy(location = location)
                                        )
                                    }
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

            Spacer(modifier = Modifier.padding(8.dp))

            Row(verticalGravity = Alignment.CenterVertically) {
                Text("Priority", color = MaterialTheme.colors.primary)

                Spacer(modifier = Modifier.padding(16.dp))

                Slider(
                        value = priorityState.value,
                        onValueChange = { priority ->
                            priorityState.value = priority
                            viewModel.project?.value?.let { proj ->
                                viewModel.updateProject(
                                    proj.copy(priority = Priority.values()[priority.toInt()])
                                )
                            }
                        },
                        valueRange = 0f..3f,
                        steps = 2
                )

            }

            Row(verticalGravity = Alignment.CenterVertically) {
                Text("Size", color = MaterialTheme.colors.primary)

                Spacer(modifier = Modifier.padding(16.dp))

                Slider(
                        value = sizeState.value,
                        onValueChange = { size ->
                            sizeState.value = size
                            viewModel.project?.value?.let { proj ->
                                viewModel.updateProject(
                                    proj.copy(
                                        size = ProjectSize.values()[size.toInt()]
                                    )
                                )
                            }
                        },
                        valueRange = 0f..3f,
                        steps = 2
                )
            }

            Row(verticalGravity = Alignment.CenterVertically) {
                Text("Cost", color = MaterialTheme.colors.primary)

                Spacer(modifier = Modifier.padding(16.dp))

                Slider(
                        value = costState.value,
                        onValueChange = { cost ->
                            costState.value = cost
                            viewModel.project?.value?.let { proj ->
                                viewModel.updateProject(
                                    proj.copy(
                                        cost = Cost.values()[cost.toInt()]
                                    )
                                )
                            }
                        },
                        valueRange = 0f..3f,
                        steps = 2
                )
            }
        }
    }

    @Composable
    fun Dialogs() {
        if (showLocationDialog) NewLocationDialog()
    }

    @Composable
    fun ProjectScreen(project: Project) {
        HomeProjectsTheme {
            Stack(modifier = Modifier.fillMaxSize().padding(32.dp)) {
                ProjectInfo(project)
                Dialogs()
            }
        }
    }

    @Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
    @Composable
    fun DefaultPreview() {
        ProjectScreen(
            Project(
                title = "Test Project",
                description = "This is just a test project in the shop",
                status = ProjectStatus.ACTIVE,
                location = Location.SHOP,
                priority = Priority.LOW,
                cost = Cost.TWO,
                size = ProjectSize.LARGE
            )
        )
    }
}