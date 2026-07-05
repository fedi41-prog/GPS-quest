package org.fedi4.gpsquest.core.ui.home

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.fedi4.gpsquest.core.data.models.Quest
import org.fedi4.gpsquest.core.ui.components.GPSStatusView
import org.fedi4.gpsquest.core.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen (
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory),
    onStartQuest: (quest:Quest) -> Unit,
    onEditQuest: (quest:Quest) -> Unit,
    onCreateQuest: () -> Unit
) {

    val quests by viewModel.quests.collectAsState()

    Log.d("HomeScreen", "quests: $quests")
    Log.d("HomeScreen", "quests size: ${quests.size}")

    Scaffold(
        topBar = { TopAppBar(
            title = { Text("GPSQuest") },
        ) },
        bottomBar = {
            BottomAppBar() {
                Text("HOME SCREEN")
            } },
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateQuest) {
                Icon(Icons.Default.Add, contentDescription = "New quest")
            }
        },
    ) { innerPadding ->

        val lazyGridState = rememberLazyGridState()
        LazyVerticalGrid(GridCells.Fixed(2), Modifier.padding(innerPadding), lazyGridState) {

            items(quests.size) {

                QuestGridItem(Modifier.padding(10.dp), quests[it], onStartQuest =  {
                    onStartQuest(quests[it])
                },onEditQuest =  {
                    onEditQuest(quests[it])
                }
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun HomeScreenPreview() {
//    HomeScreen() {}
//}