package org.fedi4.gpsquest.core.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import org.fedi4.gpsquest.core.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen (
    modifier: Modifier = Modifier, viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
) {

    Scaffold(
        topBar = { TopAppBar(
            title = { Text("GPSQuest") },
        ) },
        bottomBar = {
            BottomAppBar() {
                Text("Wow a cool quest app...")
            } },
        modifier = modifier
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            Text("check out all of the questsssss!")
            Button(onClick = { viewModel.startQuest(viewModel.quests.value!!) }) {
                Text("Start Quest")
            }
        }

    }

}