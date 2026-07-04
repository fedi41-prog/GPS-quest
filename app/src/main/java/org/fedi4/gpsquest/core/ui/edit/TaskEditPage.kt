package org.fedi4.gpsquest.core.ui.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import org.fedi4.gpsquest.core.data.models.Coordinates
import org.fedi4.gpsquest.core.data.models.QuestTask

@Composable
fun TaskEditPage(modifier: Modifier = Modifier, task: QuestTask, onSave: (QuestTask) -> Unit) {

    var name by remember { mutableStateOf(task.name) }
    var description by remember { mutableStateOf(task.description) }
    var radius by remember { mutableStateOf(task.radius.toString()) }
//    var coordinates by remember { mutableStateOf(task.coordinates) }
    var latitude by remember { mutableDoubleStateOf(task.coordinates.latitude) }
    var longitude by remember { mutableDoubleStateOf(task.coordinates.longitude) }



    Column(modifier = modifier.fillMaxSize().padding(10.dp)) {
        // TASK ID
        Text(
            text = "TASK " + (task.idx + 1).toString(),
            Modifier.fillMaxWidth().align(Alignment.CenterHorizontally).background(MaterialTheme.colorScheme.primaryContainer),
            textAlign = TextAlign.Center,
            letterSpacing = 15.sp,
            fontSize = 35.sp,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        // TASK NAME
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = radius,
            onValueChange = { radius = it },
            label = { Text("Radius (m)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = latitude.toString(),
            onValueChange = { latitude = it.toDouble() },
            label = { Text("Latitude") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = longitude.toString(),
            onValueChange = { longitude = it.toDouble() },
            label = { Text("Longitude") },
            modifier = Modifier.fillMaxWidth()
        )



        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
            onSave(task.copy(
                name = name,
                description = description,
                radius = radius.toFloatOrNull() ?: task.radius,
                coordinates = Coordinates(latitude, longitude)
            ))
        }) { Text("Save") }

        Spacer(modifier = Modifier.fillMaxHeight())
    }



//
//    Column {
//        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
//        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
//        OutlinedTextField(value = radius, onValueChange = { radius = it }, label = { Text("Radius (m)") })
//
//    }
}