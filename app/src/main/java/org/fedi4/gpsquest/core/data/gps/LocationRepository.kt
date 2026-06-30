package org.fedi4.gpsquest.core.data.gps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocationRepository(
    private val context: Context,
    private val gpsManager: GPSManager
) {

    private val _state =
        MutableStateFlow<GPSState>(GPSState.Loading)

    val state: StateFlow<GPSState> =
        _state.asStateFlow()

    fun start() {

        if (!hasPermission()) {

            _state.value = GPSState.PermissionMissing
            return
        }

        if (!gpsManager.isEnabled()) {

            _state.value = GPSState.Disabled
            return
        }

        _state.value = GPSState.Loading

        gpsManager.start {

            _state.value = GPSState.Ready(it)

        }
    }

    fun stop() {

        gpsManager.stop()

    }

    private fun hasPermission(): Boolean {

        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}