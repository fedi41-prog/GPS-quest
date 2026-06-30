package org.fedi4.gpsquest.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import org.fedi4.gpsquest.core.ui.home.HomeScreen
import org.fedi4.gpsquest.core.ui.quest.QuestScreen


@Composable
fun GPSQuestApp(
    modifier: Modifier = Modifier,
) {

    if (true) {
        QuestScreen()
    } else {
        HomeScreen()
    }

}