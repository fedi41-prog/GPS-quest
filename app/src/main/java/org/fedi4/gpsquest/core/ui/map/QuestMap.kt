package org.fedi4.gpsquest.core.ui.map

import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import org.fedi4.gpsquest.core.data.gps.GPSState
import org.maplibre.compose.camera.CameraPosition


import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.layers.CircleLayer
import org.maplibre.compose.layers.SymbolLayer
import org.maplibre.compose.location.LocationPuck
import org.maplibre.compose.location.LocationTrackingEffect
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.compose.style.BaseStyle
import org.maplibre.spatialk.geojson.Feature
import org.maplibre.spatialk.geojson.FeatureCollection
import org.maplibre.spatialk.geojson.Point
import org.maplibre.spatialk.geojson.Position
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun QuestMap( //vibe coded this one XD
    modifier: Modifier = Modifier,
    gpsState: GPSState
) {
    val cameraState = rememberCameraState()

    MaplibreMap(cameraState = cameraState,  baseStyle = BaseStyle.Uri("https://tiles.openfreemap.org/styles/liberty")) {

        if (gpsState is GPSState.Ready) {

            val position = Position(
                gpsState.location.longitude,
                gpsState.location.latitude
            )

            LaunchedEffect(position) {
                cameraState.animateTo(
                    CameraPosition(
                        target = position,
                        zoom = 15.0
                    )
                )
            }

            // Marker für Spieler zeichnen
            val source =  rememberGeoJsonSource(
                data = GeoJsonData.Features(
                    FeatureCollection(
                        Feature(
                            geometry = Point(Position(gpsState.location.longitude, gpsState.location.latitude)),
                            properties = null
                        )
                    )
                ),
            )
            // 2. Define SymbolLayer with dynamic coloring
            CircleLayer(
                id = "my-symbol-layer",
                source = source,
                // Convert a drawable resource to a MapLibre image
                // drawAsSdf = true allows us to tint the image programmatically

            )
        }
    }

}