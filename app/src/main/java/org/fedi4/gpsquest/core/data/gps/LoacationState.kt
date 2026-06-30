package org.fedi4.gpsquest.core.data.gps

data class LocationState(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
    val speed: Float,
    val bearing: Float
)