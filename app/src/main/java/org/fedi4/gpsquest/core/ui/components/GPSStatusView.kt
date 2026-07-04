package org.fedi4.gpsquest.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.fedi4.gpsquest.core.data.gps.GPSState


@Composable
fun GPSStatusView(
    modifier: Modifier = Modifier,
    gpsState: GPSState,
) {

    Column (modifier.padding(10.dp)) {
        Text("GPS Status")
        when (gpsState) {
            is GPSState.Ready -> {
                Text("GPS is ready", color = Color.Green)
                Text(gpsState.location.toString())
            }
            is GPSState.PermissionMissing -> {
                Text("GPS permission missing", color = Color.Red)
            }
            is GPSState.Disabled -> {
                Text("GPS is disabled", color = Color.Red)
            }
            is GPSState.Loading -> {
                Text("GPS is loading", color = Color.Yellow)
            }
            else -> {
                Text("GPS state unknown", color = Color.Black)
            }
        }

    }

}