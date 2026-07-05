package org.fedi4.gpsquest.core.ui.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.fedi4.gpsquest.core.data.gps.GPSState
import org.fedi4.gpsquest.core.data.models.Coordinates
import org.fedi4.gpsquest.core.data.models.QuestTask

@Composable
fun TaskEditPage(modifier: Modifier = Modifier, task: QuestTask, gps: GPSState, onChange: (QuestTask) -> Unit) {


    var name by remember(task.idx) { mutableStateOf(task.name) }
    var description by remember(task.idx) { mutableStateOf(task.description) }
    var radius by remember(task.idx) { mutableStateOf(task.radius.toString()) }
    var latitude by remember(task.idx) { mutableStateOf(task.coordinates.latitude.toString()) }
    var longitude by remember(task.idx) { mutableStateOf(task.coordinates.longitude.toString()) }

    fun pushChange() {
        onChange(
            task.copy(
                name = name,
                description = description,
                radius = radius.toFloatOrNull() ?: task.radius,
                coordinates = Coordinates(
                    latitude.toDoubleOrNull() ?: task.coordinates.latitude,
                    longitude.toDoubleOrNull() ?: task.coordinates.longitude
                )
            )
        )
    }

    Column(modifier = modifier.fillMaxSize().padding(10.dp)) {
        Text(
            text = "TASK " + (task.idx + 1).toString(),
            Modifier.fillMaxWidth().align(Alignment.CenterHorizontally).background(MaterialTheme.colorScheme.primaryContainer),
            textAlign = TextAlign.Center,
            letterSpacing = 15.sp,
            fontSize = 35.sp,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it; pushChange() },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it; pushChange() },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = radius,
            onValueChange = { radius = it; pushChange() },
            label = { Text("Radius (m)") },
            modifier = Modifier.fillMaxWidth()
        )
        HorizontalDivider()
        OutlinedTextField(
            value = latitude,
            onValueChange = { latitude = it; pushChange() },
            label = { Text("Latitude") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = longitude,
            onValueChange = { longitude = it; pushChange() },
            label = { Text("Longitude") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            enabled = gps is GPSState.Ready,
            onClick = {
                if (gps is GPSState.Ready) {
                    latitude = gps.location.latitude.toString()
                    longitude = gps.location.longitude.toString()
                    pushChange()
                }
                      }, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Set to current position")
        }


        Spacer(modifier = Modifier.fillMaxHeight())
    }
}