package org.fedi4.gpsquest.core.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.fedi4.gpsquest.R
import org.fedi4.gpsquest.core.data.gps.GPSManager
import org.fedi4.gpsquest.core.data.gps.LocationState

class LocationForegroundService : Service() {

    private lateinit var gpsManager: GPSManager
    private val binder = LocalBinder()

    private val _locationFlow = MutableStateFlow<LocationState?>(null)
    val locationFlow: StateFlow<LocationState?> = _locationFlow.asStateFlow()

    inner class LocalBinder : Binder() {
        fun getService(): LocationForegroundService = this@LocationForegroundService
    }

    override fun onCreate() {
        super.onCreate()
        gpsManager = GPSManager(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, buildNotification())

        gpsManager.start { locationState ->
            _locationFlow.value = locationState
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onDestroy() {
        gpsManager.stop()
        super.onDestroy()
    }

    private fun buildNotification(): Notification {
        val channelId = "location_tracking"
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(
            NotificationChannel(channelId, "Location Tracking", NotificationManager.IMPORTANCE_LOW)
        )
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Tracking location")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // make sure this drawable exists
            .setOngoing(true)
            .build()
    }

    companion object {
        const val NOTIFICATION_ID = 1001
    }
}