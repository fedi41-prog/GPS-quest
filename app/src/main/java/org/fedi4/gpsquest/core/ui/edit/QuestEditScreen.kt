package org.fedi4.gpsquest.core.ui.edit

import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.fedi4.gpsquest.core.data.gps.GPSState
import org.fedi4.gpsquest.core.data.models.Coordinates
import org.fedi4.gpsquest.core.data.models.QuestTask
import org.fedi4.gpsquest.core.viewmodel.QuestEditViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestEditScreen(modifier: Modifier = Modifier, viewModel: QuestEditViewModel = viewModel(factory = QuestEditViewModel.Factory), gps: GPSState) {

    val draft by viewModel.draft.collectAsState()

    Scaffold(
        topBar = { TopAppBar(
            title = { Text("GPSQuest") },
        ) },
        bottomBar = {
            BottomAppBar {
                Text("Editing Quest - ${draft?.name}", Modifier.weight(1f))
                TextButton(onClick = { viewModel.discard() }) { Text("Discard") }
                Button(onClick = { viewModel.save() }) { Text("Save") }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        Box (modifier = Modifier.padding(innerPadding)) {
            draft?.let { q ->
                val pagerState = rememberPagerState(pageCount = { q.tasks.size+1 })
                HorizontalPager(pagerState) { page ->

                    if (page == q.tasks.size) {
                        TextButton(
                            modifier = Modifier.align(Alignment.BottomCenter),
                            onClick = {viewModel.newTask()}
                        ) {
                            Text("Add task")
                        }
                        Spacer(Modifier.fillMaxSize())
                    } else {

                        TaskEditPage(
                            task = q.tasks[page],
                            onChange = {
                                task -> viewModel.onTaskEdited(task)
                            },
                            gps = gps
                        )
                    }
                }
            } ?: Text("Quest not found")
        }
    }

}