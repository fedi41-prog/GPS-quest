package org.fedi4.gpsquest.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
fun LockedTaskPage(
    modifier: Modifier = Modifier,
    task: QuestTask
) {

    Column(modifier = modifier.fillMaxSize().padding(10.dp)) {

        // TASK ID
        Text(
            text = "TASK " + (task.idx + 1).toString(),
            Modifier.fillMaxWidth().align(Alignment.CenterHorizontally).background(Color.Red),
            textAlign = TextAlign.Center,
            letterSpacing = 15.sp,
            fontSize = 35.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )


    }

}