package org.fedi4.gpsquest.core.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.fedi4.gpsquest.core.ui.components.rememberLocationPermissionRequester
import org.fedi4.gpsquest.core.ui.home.HomeScreen
import org.fedi4.gpsquest.core.ui.quest.QuestScreen


@Composable
fun AppNavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home",
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                tween(400)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                tween(400)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                tween(400)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                tween(400)
            )
        }
    ) {

        composable("home") {
            val requestPermissions = rememberLocationPermissionRequester(
                onAllGranted = {
                    navController.navigate("quest")
                }
            )
            HomeScreen(
                onStartQuest = {

                    requestPermissions()

                }
            )
        }

        composable("quest") {
            QuestScreen(
                onExit = {
                    navController.popBackStack()
                    if (navController.previousBackStackEntry == null) {
                        navController.navigate("home")
                    }
                }
            )
        }
    }
}