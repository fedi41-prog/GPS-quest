package org.fedi4.gpsquest.core.data.gps

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.IBinder
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.fedi4.gpsquest.core.service.AppForegroundService

class LocationRepository(
    private val context: Context,
    private val gpsManager: GPSManager
) {

    private val _state =
        MutableStateFlow<GPSState>(GPSState.Loading)

    val state: StateFlow<GPSState> =
        _state.asStateFlow()

    private var service: AppForegroundService? = null
    private var bound = false
    private var collectJob: Job? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            val localBinder = binder as AppForegroundService.LocalBinder
            service = localBinder.getService()
            bound = true

            collectJob = scope.launch {
                service?.locationFlow?.collect { location ->
                    if (location != null) {
                        _state.value = GPSState.Ready(location)
                    }
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bound = false
            service = null
            collectJob?.cancel()
        }
    }

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

        val intent = Intent(context, AppForegroundService::class.java)
        ContextCompat.startForegroundService(context, intent)
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    fun stop() {

        if (bound) {
            context.unbindService(connection)
            bound = false
        }

        context.stopService(Intent(context, AppForegroundService::class.java))
        collectJob?.cancel()
    }

    private fun hasPermission(): Boolean {

        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}