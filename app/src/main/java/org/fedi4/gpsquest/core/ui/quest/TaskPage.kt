package org.fedi4.gpsquest.core.ui.quest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.fedi4.gpsquest.core.data.models.QuestTask
import kotlin.concurrent.atomics.atomicArrayOfNulls

enum class TaskState {
    COMPLETED, ACTIVE, LOCKED
}

@Composable
fun TaskPage(modifier: Modifier = Modifier, task: QuestTask, state: TaskState) {

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(10.dp)) {
            // TASK ID
            Text(
                text = "TASK " + (task.idx + 1).toString(),
                Modifier.fillMaxWidth().align(Alignment.CenterHorizontally).background(
                    when (state) {
                        TaskState.COMPLETED -> Color.Green
                        TaskState.ACTIVE -> MaterialTheme.colorScheme.primaryContainer
                        TaskState.LOCKED -> Color.Gray
                    }
                ),
                textAlign = TextAlign.Center,
                letterSpacing = 15.sp,
                fontSize = 35.sp,
                color = when (state) {
                    TaskState.COMPLETED -> Color.White
                    TaskState.ACTIVE -> MaterialTheme.colorScheme.primary
                    TaskState.LOCKED -> Color.White
                },
                fontWeight = FontWeight.Bold
            )

            if (state == TaskState.LOCKED) return
            Spacer(Modifier.height(20.dp))

            Text(
                text = task.description,
                Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Thin
            )

            Spacer(modifier = Modifier.fillMaxHeight())
        }

        // Overlay
        if (state == TaskState.COMPLETED)
            Box(modifier = Modifier.matchParentSize().background(Color.Green.copy(0.2f)))
    }
}