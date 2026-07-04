package org.fedi4.gpsquest.core.data.gps

import org.fedi4.gpsquest.core.data.models.Coordinates

data class LocationState(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
    val speed: Float,
    val bearing: Float
) {
    val coordinates: Coordinates = Coordinates(latitude, longitude)
}