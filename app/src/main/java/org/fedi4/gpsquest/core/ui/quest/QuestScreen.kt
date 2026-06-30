package org.fedi4.gpsquest.core.ui.quest

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import org.fedi4.gpsquest.core.viewmodel.QuestViewModel
import androidx.compose.runtime.collectAsState
import org.fedi4.gpsquest.core.ui.components.TaskView

@Composable
fun QuestScreen(
    modifier: Modifier = Modifier,
    viewModel: QuestViewModel = viewModel(factory = QuestViewModel.Factory)
) {

    Scaffold(
        topBar = { QuestTopBar() },
        bottomBar = { QuestBottomBar() },
        modifier = modifier
    )  {
        innerPadding ->
        val pagerState = rememberPagerState() { viewModel.questState.value.tasks.size };
        HorizontalPager(pagerState, Modifier.padding(innerPadding)) {
            index ->
                TaskView(task = viewModel.questState.collectAsState().value.tasks[index])
        }

    }

    // Quest heading

    // Quest content

    //

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestTopBar(modifier: Modifier = Modifier, viewModel: QuestViewModel = viewModel(factory = QuestViewModel.Factory)) {
    TopAppBar(
        title = {
            Text(text = viewModel.questState.collectAsState().value.questName)
        },
        modifier = modifier,
        actions = {
            IconButton(onClick = {}) {
                Text("Back")
            }
        }
    )
}

@Composable
fun QuestBottomBar(modifier: Modifier = Modifier, viewModel: QuestViewModel = viewModel(factory = QuestViewModel.Factory)) {
    BottomAppBar(
        modifier = modifier
    ) {
        Text(text = "bruh a bottom bar")
    }
}