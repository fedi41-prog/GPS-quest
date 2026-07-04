package org.fedi4.gpsquest.core.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.fedi4.gpsquest.core.GPSQuestApplication
import org.fedi4.gpsquest.core.ui.components.rememberLocationPermissionRequester
import org.fedi4.gpsquest.core.ui.edit.QuestEditScreen
import org.fedi4.gpsquest.core.ui.home.HomeScreen
import org.fedi4.gpsquest.core.ui.quest.QuestScreen
import org.fedi4.gpsquest.core.viewmodel.QuestEditViewModel


@Composable
fun AppNavGraph() {

    val navController = rememberNavController()
    val questRepository = (LocalContext.current.applicationContext as GPSQuestApplication).questRepository
    val questRun by questRepository.questRun.collectAsState()
    val questEngine = (LocalContext.current.applicationContext as GPSQuestApplication).questEngine


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
                    it ->
                    questEngine.startQuest(it)
                    requestPermissions()
                },
                onEditQuest = {
                    it ->
                    navController.navigate("questEdit/${it.id}")
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

        composable(
            "questEdit/{questId}",
            arguments = listOf(navArgument("questId") { type = NavType.StringType })
        ) { backStackEntry ->
//            val questId = backStackEntry.arguments!!.getString("questId")!!
            QuestEditScreen()
        }
    }
}