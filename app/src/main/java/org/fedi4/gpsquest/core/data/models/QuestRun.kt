package org.fedi4.gpsquest.core.data.models

data class QuestRun (
    val quest: Quest,
    val progress: Int = 0,
    val distanceToNextTask: Double = 0.0,
    val startCoordinates: Coordinates,
)