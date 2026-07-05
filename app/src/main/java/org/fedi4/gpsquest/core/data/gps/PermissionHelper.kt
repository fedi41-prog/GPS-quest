package org.fedi4.gpsquest.core.data.gps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

object LocationPermissionHelper {

    fun hasFineLocation(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun hasBackgroundLocation(context: Context): Boolean {
        // Background permission only exists from API 29+; below that,
        // foreground location permission already covers background use.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) return true

        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun hasAllRequiredPermissions(context: Context): Boolean {
        return hasFineLocation(context) //&& hasBackgroundLocation(context)
    }
}