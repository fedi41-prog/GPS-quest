package org.fedi4.gpsquest.core.ui.edit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.fedi4.gpsquest.core.data.models.QuestTask
import org.fedi4.gpsquest.core.viewmodel.QuestEditViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestEditScreen(modifier: Modifier = Modifier, viewModel: QuestEditViewModel = viewModel(factory = QuestEditViewModel.Factory)) {

    val quest by viewModel.quest.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(
            title = { Text("GPSQuest") },
        ) },
        bottomBar = {
            BottomAppBar() {
                Text("Editing Quest - ${quest?.name}")
            } },
        modifier = modifier
    ) { innerPadding ->
        Box (modifier = Modifier.padding(innerPadding)) {
            quest?.let { q ->
                val pagerState = rememberPagerState(pageCount = { q.tasks.size })
                HorizontalPager(pagerState) { page ->

                    TaskEditPage(
                        task = q.tasks[page],
                        onSave = { updated ->
                            viewModel.onTaskEdited(
                                updated
                            )
                        }
                    )

                }
            } ?: Text("Quest not found")
        }
    }

}