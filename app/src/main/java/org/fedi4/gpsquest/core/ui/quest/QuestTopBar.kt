package org.fedi4.gpsquest.core.ui.quest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.fedi4.gpsquest.core.ui.components.heatColor
import org.fedi4.gpsquest.core.ui.components.heatLabel
import org.fedi4.gpsquest.core.viewmodel.QuestViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestTopBar(modifier: Modifier = Modifier, viewModel: QuestViewModel = viewModel(factory = QuestViewModel.Factory), onExit: () -> Unit) {
    val questRun by viewModel.questRun.collectAsState()
    val quests by viewModel.quests.collectAsState()
//    val distanceToNextTask =  questRun?.distanceToNextTask ?: 0.0
//    val distanceProgress = (1000f / distanceToNextTask)

    TopAppBar(
        title = {

            Row {
                Text(text = "GPS-Quest", fontSize = 20.sp, fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(10.dp), color = MaterialTheme.colorScheme.tertiary)
                Text(
                    text = questRun?.quest?.name ?: "",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.padding(10.dp)
                )
            }
        },
        modifier = modifier,
        actions = {
            IconButton(onClick = {onExit()}) {
                Text("EXIT")
            }
        },
//        colors = TopAppBarDefaults.topAppBarColors(
//            containerColor = heatColor(distanceProgress.toFloat()),
//        )
    )
}