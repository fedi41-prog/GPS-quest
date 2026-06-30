package org.fedi4.gpsquest.core.data.models

data class QuestState (
    var quest: Quest? = null,

    var questName: String = "",

    var tasks: List<TaskState> = emptyList(),
    var progress: Int = 0,
    var finished: Boolean = false
)