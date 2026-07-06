package org.fedi4.gpsquest.core.ui.home

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.fedi4.gpsquest.core.data.models.Quest

@Composable
fun QuestGridItem(modifier: Modifier = Modifier, quest: Quest, onStartQuest: () -> Unit, onEditQuest: () -> Unit) {

    Card (modifier = modifier, shape = RoundedCornerShape(20.dp),) {
        Column {

            Row(Modifier.fillMaxWidth().padding(5.dp)) {
                Text(modifier = Modifier.fillMaxWidth().align(Alignment.CenterVertically), text=quest.name, textAlign = TextAlign.Center, fontWeight = FontWeight.Medium, fontSize = 15.sp)
                //Text(modifier = Modifier.fillMaxWidth(0.3f).align(Alignment.CenterVertically), text=quest.tasks.size.toString(), textAlign = TextAlign.Center)
            }

            Column() {


                Surface(
                    modifier = Modifier.fillMaxWidth()
                        .clickable { onEditQuest() },
                    color = MaterialTheme.colorScheme.primary,
                    //                 shape = RoundedCornerShape(bottomStart = 20.dp)
                ) {
                    Text(
                        "EDIT",
                        modifier = Modifier.padding(4.dp),
                        textAlign = TextAlign.Center
                    )
                }
                HorizontalDivider()
                Surface(
                    modifier = Modifier.fillMaxWidth()
                        .clickable { onStartQuest() },
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp)
                ) {
                    Text(
                        "START",
                        modifier = Modifier.padding(4.dp),
                        textAlign = TextAlign.Center
                    )
                }

            }

        }
    }

}