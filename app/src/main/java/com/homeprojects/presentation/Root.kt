package com.homeprojects.presentation

import androidx.compose.runtime.Composable
import com.github.zsoltk.compose.router.Router

interface Root {

    sealed class Routing {
        object HomeScreen : Routing()
        object ProjectScreen : Routing()
    }

    companion object {
        @Composable
        fun Content(defaultRouting: Routing = Routing.HomeScreen) {
            Router(defaultRouting) { backStack ->
                when (val routing = backStack.last()) {
                    is Routing.HomeScreen -> HomeScreen.Content()
                    is Routing.ProjectScreen -> ProjectScreen.Content()
                }
            }
        }
    }
}