package com.homeprojects.presentation.locations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.homeprojects.model.Location
import com.homeprojects.ui.HomeProjectsTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LocationsFragment : Fragment() {

    @Inject lateinit var viewModel: LocationsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                HomeProjectsTheme {
                    LocationsScreen(viewModel.locations)
                }
            }
        }
    }

    @Composable
    fun LocationsScreen(locationsLiveData: LiveData<List<Location>>) {
        val locations = locationsLiveData.observeAsState(emptyList())

        LazyColumnFor(items = locations.value.sortedBy { it.name }) { location ->
            Column(
                    modifier = Modifier.clickable(
                        onClick = {},
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
}