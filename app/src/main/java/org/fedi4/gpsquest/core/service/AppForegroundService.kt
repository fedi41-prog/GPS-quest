package org.fedi4.gpsquest.core.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.fedi4.gpsquest.R
import org.fedi4.gpsquest.core.data.gps.GPSManager
import org.fedi4.gpsquest.core.data.gps.LocationState
import kotlin.getValue
import org.fedi4.gpsquest.core.GPSQuestApplication
import kotlin.time.Duration.Companion.milliseconds

class AppForegroundService : Service() {

    private lateinit var gpsManager: GPSManager
    private val binder = LocalBinder()

    private var vibrationJob: Job? = null
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    // Zugriff auf QuestEngine über die Application
    private val questEngine by lazy {
        (application as GPSQuestApplication).questEngine
    }
    private val _locationFlow = MutableStateFlow<LocationState?>(null)
    val locationFlow: StateFlow<LocationState?> = _locationFlow.asStateFlow()

    private fun startVibrationLoop() {
        if (vibrationJob?.isActive == true) return
        vibrationJob = serviceScope.launch {
            while (true) {
                VibrationHelper.vibrate(this@AppForegroundService, 400L)
                delay(600L.milliseconds)
            }
        }
    }

    private fun stopVibrationLoop() {
        vibrationJob?.cancel()
        vibrationJob = null
    }

    inner class LocalBinder : Binder() {
        fun getService(): AppForegroundService = this@AppForegroundService
    }

    override fun onCreate() {
        super.onCreate()
        gpsManager = GPSManager(this)

        serviceScope.launch {
            questEngine.awaitingConfirmation.collect { awaiting ->
                if (awaiting) startVibrationLoop() else stopVibrationLoop()
            }
        }
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
        stopVibrationLoop()
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