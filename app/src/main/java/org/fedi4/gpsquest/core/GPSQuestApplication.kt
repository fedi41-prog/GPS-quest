package org.fedi4.gpsquest.core

import android.app.Application
import org.fedi4.gpsquest.core.data.gps.GPSManager
import org.fedi4.gpsquest.core.data.gps.LocationRepository
import org.fedi4.gpsquest.core.data.quest.QuestEngine
import org.fedi4.gpsquest.core.data.quest.QuestRepository


class GPSQuestApplication : Application() {

    val gpsManager by lazy {
        GPSManager(this)
    }


    val locationRepository by lazy {
        LocationRepository(this, gpsManager)
    }
    val questRepository by lazy {
        QuestRepository()
    }

    val questEngine = QuestEngine(questRepository)
}