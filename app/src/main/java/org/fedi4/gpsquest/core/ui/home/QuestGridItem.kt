package org.fedi4.gpsquest.core.ui.home

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import org.fedi4.gpsquest.core.data.models.Quest

@Composable
fun QuestGridItem(modifier: Modifier = Modifier, quest: Quest, onStartQuest: () -> Unit) {

    Card (modifier = modifier) {
        Column {

            Row(Modifier.fillMaxWidth()) {
                Text(modifier = Modifier.fillMaxWidth(0.5f).align(Alignment.CenterVertically), text=quest.name, textAlign = TextAlign.Center)
                Text(modifier = Modifier.fillMaxWidth(0.5f).align(Alignment.CenterVertically), text=quest.tasks.size.toString(), textAlign = TextAlign.Center)
            }

            Button(modifier = Modifier.fillMaxWidth(), onClick = {
                onStartQuest()
                Log.d("QuestGridItem", "clicked")
            }) {
                Text("START")
            }

        }
    }

}