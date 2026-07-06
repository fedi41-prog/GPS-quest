package org.fedi4.gpsquest.core.ui.edit

import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.fedi4.gpsquest.core.data.gps.GPSState
import org.fedi4.gpsquest.core.data.models.Coordinates
import org.fedi4.gpsquest.core.data.models.QuestTask
import org.fedi4.gpsquest.core.viewmodel.QuestEditViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestEditScreen(
    modifier: Modifier = Modifier,
    viewModel: QuestEditViewModel = viewModel(factory = QuestEditViewModel.Factory),
    gps: GPSState,
    onExit: () -> Unit = {}
) {

    val draft by viewModel.draft.collectAsState()
    val deleted by viewModel.deleted.collectAsState()
    var editingTasks by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    // Nach dem Löschen automatisch den Screen verlassen
    LaunchedEffect(deleted) {
        if (deleted) onExit()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GPSQuest") },
            )
        },
        bottomBar = {
            BottomAppBar {
                Text(
                    if (editingTasks) "Editing tasks - ${draft?.name}" else "Editing Quest - ${draft?.name}",
                    Modifier.weight(1f)
                )
                if (editingTasks) {
                    TextButton(onClick = { editingTasks = false }) { Text("Back") }
                } else {
                    TextButton(onClick = { viewModel.discard() }) { Text("Discard") }
                }
                Button(onClick = { viewModel.save() }) { Text("Save") }
            }
        },
        modifier = modifier
    ) { innerPadding ->

        Box(Modifier.padding(innerPadding)) {
            if (editingTasks) {
                draft?.let { q ->
                    TasksEditScreen(tasks = q.tasks, viewModel = viewModel, gps = gps)
                } ?: Text("Quest not found")
            } else {
                draft?.let { q ->
                    QuestDataEditPage(
                        name = q.name,
                        taskCount = q.tasks.size,
                        onNameChange = { viewModel.updateQuestName(it) },
                        onEditTasks = { editingTasks = true },
                        onDeleteQuest = { showDeleteConfirm = true }
                    )
                } ?: Text("Quest not found")
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete quest?") },
            text = { Text("This will permanently delete \"${draft?.name}\" and all its tasks.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirm = false
                        viewModel.deleteQuest()
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = androidx.compose.ui.graphics.Color.Red)
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
private fun QuestDataEditPage(
    modifier: Modifier = Modifier,
    name: String,
    taskCount: Int,
    onNameChange: (String) -> Unit,
    onEditTasks: () -> Unit,
    onDeleteQuest: () -> Unit
) {
    // lokaler Text-State, damit das Textfeld beim Tippen nicht "springt"
    var localName by remember(name) { mutableStateOf(name) }

    Column(modifier = modifier.fillMaxSize().padding(10.dp)) {

        OutlinedTextField(
            value = localName,
            onValueChange = {
                localName = it
                onNameChange(it)
            },
            label = { Text("Quest name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        Text("Tasks: $taskCount")

        Spacer(Modifier.height(10.dp))

        Button(onClick = onEditTasks, modifier = Modifier.fillMaxWidth()) {
            Text("Edit tasks")
        }

        Spacer(Modifier.height(20.dp))
        HorizontalDivider()
        Spacer(Modifier.height(20.dp))

        Button(
            onClick = onDeleteQuest,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = androidx.compose.ui.graphics.Color.Red
            )
        ) {
            Text("Delete quest")
        }
    }
}