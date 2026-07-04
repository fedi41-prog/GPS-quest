package org.fedi4.gpsquest.core

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import org.fedi4.gpsquest.core.data.gps.GPSManager
import org.fedi4.gpsquest.core.data.gps.LocationRepository
import org.fedi4.gpsquest.core.data.quest.QuestEngine
import org.fedi4.gpsquest.core.data.quest.QuestRepository
import org.fedi4.gpsquest.core.data.quest.QuestStorage
import org.fedi4.gpsquest.core.service.VibrationHelper
import org.maplibre.android.MapLibre
import org.maplibre.android.WellKnownTileServer


class GPSQuestApplication : Application() {

    val gpsManager by lazy {
        GPSManager(this)
    }


    val locationRepository by lazy {
        LocationRepository(this, gpsManager)
    }

    val questStorage by lazy {
        QuestStorage(this)
    }

    val questRepository by lazy {
        QuestRepository(questStorage)
    }


    val questEngine by lazy {
        QuestEngine(
            repository = questRepository,
            onTaskCompleted = {
                VibrationHelper.vibrate(this)
            }
        )
    }

    override fun onCreate() {
        super.onCreate()
//        questRepository.addTestQuest()
    }
}