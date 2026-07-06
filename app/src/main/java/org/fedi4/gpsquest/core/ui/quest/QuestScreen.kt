package org.fedi4.gpsquest.core.ui.quest

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import org.fedi4.gpsquest.core.GPSQuestApplication
import org.fedi4.gpsquest.core.data.gps.GPSState
import org.fedi4.gpsquest.core.viewmodel.QuestViewModel
import org.fedi4.gpsquest.core.ui.components.GPSStatusView
import org.fedi4.gpsquest.core.ui.components.MultipleLinearProgressIndicator
import org.fedi4.gpsquest.core.ui.components.rememberLocationPermissionRequester
import org.fedi4.gpsquest.core.ui.map.QuestMap
import kotlin.math.min


@Composable
fun QuestScreen(
    modifier: Modifier = Modifier,
    viewModel: QuestViewModel = viewModel(factory = QuestViewModel.Factory),
    onExit: () -> Unit
) {
    val questRun by viewModel.questRun.collectAsState()
    val progress = questRun?.progress ?: 0
    val app = LocalContext.current.applicationContext as GPSQuestApplication
    val locationRepository = app.locationRepository
    val gpsState by locationRepository.state.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> locationRepository.start()
                Lifecycle.Event.ON_STOP -> locationRepository.stop()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Scaffold(
        topBar = { QuestTopBar(onExit = onExit) },
        bottomBar = { QuestBottomBar(Modifier.fillMaxWidth()) },
        modifier = modifier
    ) { innerPadding ->

        if (questRun?.finished == true) {
            Box(Modifier.padding(innerPadding)) {
                Text("QUEST COMPLETED!", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold, fontStyle = FontStyle.Italic, textAlign = TextAlign.Center, modifier = Modifier.fillMaxSize())
            }
        } else {

            val pagerState = rememberPagerState() {
                min(
                    (questRun?.progress ?: -1) + 1,
                    questRun?.quest?.tasks?.size ?: 0
                )
            };
            LaunchedEffect(progress) {
                questRun?.let {
                    pagerState.animateScrollToPage(it.progress)
                }
            }

            // ELEMENTS
            questRun?.let { run ->
                Column(Modifier.padding(innerPadding)) {
                    HorizontalPager(pagerState, modifier = Modifier) { index ->
                        val state = when {
                            index < progress -> TaskState.COMPLETED // COMPLETED
                            index == progress -> TaskState.ACTIVE    // ACTIVE
                            else -> TaskState.LOCKED    // LOCKED
                        }
                        TaskPage(task = run.quest.tasks[index], state = state)

                    }
                    //Spacer(modifier = Modifier.height(10.dp))
                    //GPSStatusView(modifier = Modifier.fillMaxWidth().height(50.dp), gpsState = gpsState)

                    //QuestMap(Modifier.fillMaxHeight(0.5f), gpsState = gpsState)
                }
            }
        }
    }

}


@SuppressLint("ServiceCast")
fun vibrateOnTargetReached(context: Context) {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vm.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(600, VibrationEffect.EFFECT_HEAVY_CLICK))
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(600)
    }
}


