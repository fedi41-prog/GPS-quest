package org.fedi4.gpsquest.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.fedi4.gpsquest.core.data.models.TaskState


@Composable
fun TaskView(modifier: Modifier = Modifier, task: TaskState) {

    Column(modifier = modifier) {
        Text(text = task.taskName)
        Text(text = task.taskDescription)
        Text(text = task.task?.coordinates?.latitude.toString())
        Text(text = task.task?.coordinates?.longitude.toString())
        Text(text = task.task?.radius.toString())
        Text(text = task.finished.toString())
    }
}