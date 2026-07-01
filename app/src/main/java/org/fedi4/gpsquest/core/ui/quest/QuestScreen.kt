package org.fedi4.gpsquest.core.ui.quest

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.fedi4.gpsquest.core.data.gps.GPSState
import org.fedi4.gpsquest.core.viewmodel.QuestViewModel
import org.fedi4.gpsquest.core.ui.components.ActiveTaskPage
import org.fedi4.gpsquest.core.ui.components.CompletedTaskPage
import org.fedi4.gpsquest.core.ui.components.LockedTaskPage
import org.fedi4.gpsquest.core.ui.map.QuestMap


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun QuestScreen(
    modifier: Modifier = Modifier,
    viewModel: QuestViewModel = viewModel(factory = QuestViewModel.Factory),
    onExit: () -> Unit
) {
    val quests by viewModel.quests.collectAsState()
    val questRun by viewModel.questRun.collectAsState()
    val gpsState by viewModel.gpsState.collectAsState()
    val progress = questRun?.progress ?: 0

    Scaffold(
        topBar = { QuestTopBar(onExit = onExit) },
        bottomBar = { QuestBottomBar() },
        floatingActionButton = {
            IconButton(
                onClick = { viewModel.next() },
                modifier = Modifier.padding(10.dp),
                enabled = progress < (questRun?.quest?.tasks?.size ?: 0) - 1,
                ) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = null
                )
            }
        },
        modifier = modifier
    )  {
        innerPadding ->


        val pagerState = rememberPagerState() { (questRun?.progress ?: -1) + 1 };
        LaunchedEffect(progress) {
            questRun?.let {
                pagerState.animateScrollToPage(it.progress)
            }
        }

        // ELEMENTS
        Column(Modifier.padding(innerPadding)) {
            HorizontalPager(pagerState, modifier = Modifier.fillMaxHeight(0.5f)) { index ->
                when {
                    index < progress -> CompletedTaskPage(task = questRun!!.quest.tasks[index]) // COMPLETED
                    index == progress -> ActiveTaskPage(task = questRun!!.quest.tasks[index])    // ACTIVE
                    else -> LockedTaskPage(task = questRun!!.quest.tasks[index])    // LOCKED
                }
            }

            QuestMap(Modifier.fillMaxHeight(0.5f), gpsState = gpsState)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestTopBar(modifier: Modifier = Modifier, viewModel: QuestViewModel = viewModel(factory = QuestViewModel.Factory), onExit: () -> Unit) {
    val questRun by viewModel.questRun.collectAsState()
    val quests by viewModel.quests.collectAsState()


    TopAppBar(
        title = {
            Text(text = questRun?.quest?.name?:"")
        },
        modifier = modifier,
        actions = {
            IconButton(onClick = {onExit()}) {
                Text("EXIT")
            }
        }
    )
}

@Composable
fun QuestBottomBar(modifier: Modifier = Modifier, viewModel: QuestViewModel = viewModel(factory = QuestViewModel.Factory)) {
    val gpsState by viewModel.gpsState.collectAsState()

    BottomAppBar(
        modifier = modifier,
    ) {
        when (gpsState) {
            is GPSState.Disabled -> {
                Text(text = "GPS is disabled")
            }
            is GPSState.Loading -> {
                Text(text = "GPS loading")
            }
            is GPSState.PermissionMissing -> {
                Text(text = "GPS permission missing")

            }
            is GPSState.Ready -> {
                Text(text = "GPS ready")
                Text(text = "GPS location: ${(gpsState as GPSState.Ready).location.latitude}, ${(gpsState as GPSState.Ready).location.longitude}")
            }
        }

    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->

        if (granted) {
            viewModel.refreshGPS()
        }
    }

    LaunchedEffect(gpsState) {
        if (gpsState == GPSState.PermissionMissing) {
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}