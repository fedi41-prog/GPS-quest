package org.fedi4.gpsquest.core.data.models

import android.location.Location
import kotlinx.serialization.Serializable

@Serializable
data class Coordinates(
    val latitude: Double,
    val longitude: Double
) {
    fun distanceTo(other: Coordinates): Double {
        val results = FloatArray(1)

        Location.distanceBetween(
            latitude,
            longitude,
            other.latitude,
            other.longitude,
            results
        )

        return results[0].toDouble() // Meter
    }
}