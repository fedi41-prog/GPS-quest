package org.fedi4.gpsquest.core.data.models

data class Quest(
    val name : String,
    val tasks : List<QuestTask>
)

data class QuestTask(
    val idx: Int,
    val name: String,
    val coordinates: Coordinates,
    val description: String,
    val radius: Float
)

