package com.homeprojects.presentation.locations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.homeprojects.model.Project
import com.homeprojects.presentation.projects.ProjectListItem
import com.homeprojects.ui.HomeProjectsTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LocationsFragment : Fragment() {

    @Inject lateinit var viewModel: LocationsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            when (viewModel.currentScreen.value) {
                LocationScreen.LOCATIONS -> findNavController().navigateUp()
                LocationScreen.PROJECTS_FOR_LOCATION -> viewModel.currentScreen.value = LocationScreen.LOCATIONS
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                HomeProjectsTheme {
                    LocationsScreen(
                        viewModel = viewModel,
                        navController = findNavController()
                    )
                }
            }
        }
    }


}

@Composable
fun ListLocations(
        viewModel: LocationsViewModel,
        screenState: State<LocationScreen?>
) {
    val locations = viewModel.locations.observeAsState(emptyList())

    LazyColumnFor(items = locations.value.sortedBy { it.name }) { location ->
        Column(
                modifier = Modifier.clickable(
                        onClick = {
                            viewModel.selectedLocation = location
                            viewModel.currentScreen.value = LocationScreen.PROJECTS_FOR_LOCATION
                        },
                        onLongClick = {
                            viewModel.deleteLocation(location)
                        }
                )
        ) {
            Text(
                    text = location.name,
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(16.dp)
            )
            Divider()
        }
    }
}

@Composable
fun ListProjectsForLocation(
        viewModel: LocationsViewModel,
        onProjectClick: (project: Project) -> Unit
) {
    val projects = viewModel.projectsForLocation?.observeAsState(emptyList())

    ScrollableColumn {
        projects?.value?.forEach { project ->
            ProjectListItem(
                    project,
                    onClick = { onProjectClick(project) },
                    onLongClick = { viewModel.deleteProject(project) }
            )
        }
    }
}

@Composable
fun LocationsScreen(
        viewModel: LocationsViewModel,
        navController: NavController
) {
    val screenState = viewModel.currentScreen.observeAsState()

    when (screenState.value) {
        LocationScreen.LOCATIONS -> {
            ListLocations(
                    viewModel = viewModel,
                    screenState = screenState
            )
        }
        LocationScreen.PROJECTS_FOR_LOCATION -> {
            ListProjectsForLocation(
                    viewModel = viewModel,
                    onProjectClick = { project ->
                        val direction = LocationsFragmentDirections.editProject(project.id)
                        navController.navigate(direction)
                    }
            )
        }
    }
}