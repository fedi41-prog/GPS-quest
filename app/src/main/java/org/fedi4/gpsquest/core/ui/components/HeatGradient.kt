package org.fedi4.gpsquest.core.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

// HEAT GRADIENT
data class HeatStop(val at: Float, val color: Color)
val DefaultHeatStops = listOf(
    HeatStop(0f, Color(0xFF3B82F6)),   // far / cold — blue
    HeatStop(50f, Color(0xFFA855F7)),  // getting warmer — purple
    HeatStop(100f, Color(0xFFEF4444)), // close / hot — red
)

fun heatColor(percent: Float, stops: List<HeatStop> = DefaultHeatStops): Color {
    val p = percent.coerceIn(0f, 100f)
    val sorted = stops.sortedBy { it.at }
    var lower = sorted.first()
    var upper = sorted.last()
    for (i in 0 until sorted.size - 1) {
        if (p >= sorted[i].at && p <= sorted[i + 1].at) {
            lower = sorted[i]
            upper = sorted[i + 1]
            break
        }
    }
    val span = (upper.at - lower.at).let { if (it == 0f) 1f else it }
    val t = (p - lower.at) / span
    return lerp(lower.color, upper.color, t)
}

fun heatLabel(percent: Float): String = when {
    percent < 20f -> "Freezing"
    percent < 40f -> "Cold"
    percent < 60f -> "Warm"
    percent < 80f -> "Hot"
    else -> "Burning up"
}

