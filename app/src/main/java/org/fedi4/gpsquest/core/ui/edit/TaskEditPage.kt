package org.fedi4.gpsquest.core.ui.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.fedi4.gpsquest.core.data.gps.GPSState
import org.fedi4.gpsquest.core.data.models.Coordinates
import org.fedi4.gpsquest.core.data.models.QuestTask
import org.fedi4.gpsquest.core.viewmodel.QuestEditViewModel

@Composable
fun TaskEditPage(
    modifier: Modifier = Modifier,
    task: QuestTask,
    gps: GPSState,
    canMoveLeft: Boolean = false,
    canMoveRight: Boolean = false,
    onChange: (QuestTask) -> Unit,
    onDelete: () -> Unit = {},
    onMoveLeft: () -> Unit = {},
    onMoveRight: () -> Unit = {}
) {

    var description by remember(task.idx) { mutableStateOf(task.description) }
    var radius by remember(task.idx) { mutableStateOf(task.radius.toString()) }
    var latitude by remember(task.idx) { mutableStateOf(task.coordinates.latitude.toString()) }
    var longitude by remember(task.idx) { mutableStateOf(task.coordinates.longitude.toString()) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    fun pushChange() {
        onChange(
            task.copy(
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

        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onMoveLeft, enabled = canMoveLeft) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Move left")
            }

            Text(
                text = "TASK " + (task.idx + 1).toString(),
                Modifier
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                textAlign = TextAlign.Center,
                letterSpacing = 15.sp,
                fontSize = 35.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            IconButton(onClick = onMoveRight, enabled = canMoveRight) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Move right")
            }
        }

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
        Row(Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = latitude,
                onValueChange = { latitude = it; pushChange() },
                label = { Text("Latitude") },
                modifier = Modifier.weight(0.5f)
            )
            Spacer(Modifier.width(10.dp))
            OutlinedTextField(
                value = longitude,
                onValueChange = { longitude = it; pushChange() },
                label = { Text("Longitude") },
                modifier = Modifier.weight(0.5f)
            )
        }

        Button(
            enabled = gps is GPSState.Ready,
            onClick = {
                if (gps is GPSState.Ready) {
                    latitude = gps.location.latitude.toString()
                    longitude = gps.location.longitude.toString()
                    pushChange()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Set to current position")
        }

        Spacer(Modifier.height(50.dp))

        Button(
            onClick = { showDeleteConfirm = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Icon(Icons.Default.Delete, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Delete task")
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete task?") },
            text = { Text("This will permanently remove \"${task.description}\" from the quest.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirm = false
                        onDelete()
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun TasksEditScreen(
    modifier: Modifier = Modifier,
    tasks: List<QuestTask>,
    viewModel: QuestEditViewModel = viewModel(factory = QuestEditViewModel.Factory),
    gps: GPSState
) {

    val pagerState = rememberPagerState(pageCount = { tasks.size + 1 })

    HorizontalPager(pagerState, modifier = modifier) { page ->

        if (page == tasks.size) {
            TextButton(
                modifier = Modifier,
                onClick = { viewModel.newTask(); viewModel.save() }
            ) {
                Text("Add task")
            }
            Spacer(Modifier.fillMaxSize())
        } else {

            TaskEditPage(
                task = tasks[page],
                gps = gps,
                canMoveLeft = page > 0,
                canMoveRight = page < tasks.size - 1,
                onChange = { task -> viewModel.onTaskEdited(task); viewModel.save() },
                onDelete = { viewModel.removeTask(tasks[page].idx); viewModel.save() },
                onMoveLeft = {
                    val newOrder = tasks.toMutableList()
                    newOrder[page] = tasks[page - 1].also { newOrder[page - 1] = tasks[page] }
                    viewModel.reorderTasks(newOrder); viewModel.save()
                },
                onMoveRight = {
                    val newOrder = tasks.toMutableList()
                    newOrder[page] = tasks[page + 1].also { newOrder[page + 1] = tasks[page] }
                    viewModel.reorderTasks(newOrder); viewModel.save()
                }
            )
        }
    }
}