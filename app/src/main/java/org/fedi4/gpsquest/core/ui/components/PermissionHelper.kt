package org.fedi4.gpsquest.core.ui.components

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.fedi4.gpsquest.core.data.gps.LocationPermissionHelper


//claude cooked this one for me
@Composable
fun rememberLocationPermissionRequester(
    onAllGranted: () -> Unit
): () -> Unit {

    val context = LocalContext.current

    // Step 2: background permission (only asked after fine location is granted)
    val backgroundLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        // Even if the user denies background, foreground tracking still works —
        // just call onAllGranted() regardless, or gate on `granted` if you require it.
        onAllGranted()
    }

    // Step 1: fine location
    val fineLocationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
                !LocationPermissionHelper.hasBackgroundLocation(context)
            ) {
                backgroundLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            } else {
                onAllGranted()
            }
        }
        // else: denied — surface GPSState.PermissionMissing, which your repo already does
    }

    return {
        if (LocationPermissionHelper.hasFineLocation(context)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
                !LocationPermissionHelper.hasBackgroundLocation(context)
            ) {
                backgroundLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            } else {
                onAllGranted()
            }
        } else {
            fineLocationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}