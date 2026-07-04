package org.fedi4.gpsquest.core.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.fedi4.gpsquest.core.data.models.QuestTask


@Composable
fun CompletedTaskPage(modifier: Modifier = Modifier, task: QuestTask) {

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(10.dp)) {
            // TASK ID
            Text(
                text = "TASK " + (task.idx + 1).toString(),
                Modifier.fillMaxWidth().align(Alignment.CenterHorizontally).background(Color.Green),
                textAlign = TextAlign.Center,
                letterSpacing = 15.sp,
                fontSize = 35.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            // TASK NAME
            Text(
                text = task.name,
                Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Thin
            )
            Text(
                text = task.description,
                Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Thin
            )
            Text(
                text = task.coordinates.latitude.toString(),
                Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
            )
            Text(
                text = task.coordinates.longitude.toString(),
                Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
            )
            Text(
                text = task.radius.toString(),
                Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
            )


            Spacer(modifier = Modifier.fillMaxHeight())
        }

        // Overlay
        Box(modifier = Modifier.matchParentSize().background(Color.White.copy(0.2f)))
    }
}