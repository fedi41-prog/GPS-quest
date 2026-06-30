package org.fedi4.gpsquest.core.data.gps


import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.os.Looper
import com.google.android.gms.location.*

class GPSManager(
    private val context: Context
) {

    private val client =
        LocationServices.getFusedLocationProviderClient(context)

    private var callback: LocationCallback? = null

    fun isEnabled(): Boolean {

        val manager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    fun start(
        onLocation: (LocationState) -> Unit
    ) {

        if (callback != null)
            return

        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            1000L
        )
            .setMinUpdateDistanceMeters(1f)
            .build()

        callback = object : LocationCallback() {

            override fun onLocationResult(result: LocationResult) {

                val location = result.lastLocation ?: return

                onLocation(
                    LocationState(
                        latitude = location.latitude,
                        longitude = location.longitude,
                        accuracy = location.accuracy,
                        speed = location.speed,
                        bearing = location.bearing
                    )
                )
            }
        }

        client.requestLocationUpdates(
            request,
            callback!!,
            Looper.getMainLooper()
        )
    }

    fun stop() {

        callback?.let {
            client.removeLocationUpdates(it)
        }

        callback = null
    }
}