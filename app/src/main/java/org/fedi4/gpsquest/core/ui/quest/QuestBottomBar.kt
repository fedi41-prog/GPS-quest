package org.fedi4.gpsquest.core.ui.quest

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.fedi4.gpsquest.core.data.gps.GPSState
import org.fedi4.gpsquest.core.ui.components.MultipleLinearProgressIndicator
import org.fedi4.gpsquest.core.ui.components.heatColor
import org.fedi4.gpsquest.core.ui.components.heatLabel
import org.fedi4.gpsquest.core.viewmodel.QuestViewModel

@Composable
fun QuestBottomBar(modifier: Modifier = Modifier, viewModel: QuestViewModel = viewModel(factory = QuestViewModel.Factory)) {
    val gpsState by viewModel.gpsState.collectAsState()
    val questRun by viewModel.questRun.collectAsState()
    val progress = questRun?.progress ?: 0
    val distance = questRun?.distanceToNextTask ?: 0f

    val taskProgressPercent = viewModel.currentTaskProgress(distance.toFloat())*100


    Surface(
        modifier = Modifier.navigationBarsPadding()
    ) {
        Column(
            Modifier
                .fillMaxWidth().height(50.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth().height(40.dp).background(heatColor(taskProgressPercent))) {
                Text(heatLabel(taskProgressPercent), Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontSize = 25.sp, color = Color.White)
            }

            MultipleLinearProgressIndicator(
                modifier = Modifier.fillMaxHeight().height(10.dp),
                primaryProgress = { (progress.toFloat()) / (questRun?.quest?.tasks?.size ?: 1).toFloat() },
                secondaryProgress = { (progress.toFloat()+1) / (questRun?.quest?.tasks?.size ?: 1).toFloat() },
                clipShape = CutCornerShape(0.dp)
            )
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