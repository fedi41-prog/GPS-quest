package org.fedi4.gpsquest.core.data.models

data class TaskState(
    var task: QuestTask? = null,

    var taskName: String = "",
    var taskDescription: String = "",

    var finished: Boolean = false
)