package org.fedi4.gpsquest.core.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Quest(
    val id : String,
    val name : String,
    val tasks : List<QuestTask>,
)

@Serializable
data class QuestTask(
    val idx: Int,
    val name: String,
    val coordinates: Coordinates,
    val description: String,
    val radius: Float
)

