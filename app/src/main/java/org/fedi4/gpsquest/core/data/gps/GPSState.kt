package org.fedi4.gpsquest.core.data.gps


sealed interface GPSState {

    data object Loading : GPSState

    data object PermissionMissing : GPSState

    data object Disabled : GPSState

    data class Ready(
        val location: LocationState
    ) : GPSState
}