package org.fedi4.gpsquest.core

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import org.fedi4.gpsquest.core.data.gps.GPSManager
import org.fedi4.gpsquest.core.data.gps.LocationRepository
import org.fedi4.gpsquest.core.data.models.Coordinates
import org.fedi4.gpsquest.core.data.models.Quest
import org.fedi4.gpsquest.core.data.models.QuestTask
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
        addTestQuest()
    }


    fun addTestQuest() {
        if (questRepository.quests.value.isNotEmpty()) return

        questRepository.addQuest(
            Quest(
                id = "test1",
                name = "cool quest bruh",
                tasks = listOf(
                    QuestTask(0, "abracadabra", Coordinates(latitude=51.3268165, longitude=12.335707), "first task", 100f),
                    QuestTask(1, "cadabrababra", Coordinates(latitude=51.3268165, longitude=12.335707), "firdawdwst task", 100f),
                    QuestTask(2, "tralalerotralala", Coordinates(latitude=51.3268165, longitude=12.335707), "first taddsk", 100f),
                    QuestTask(3, "tungtungtungsahur ", Coordinates(latitude=51.3268165, longitude=12.335707), "first dwda task", 100f)
                ),
                startLocation = Coordinates(latitude=51.3268165, longitude=12.335707)
            ),
            )
        questRepository.addQuest(
            Quest(
                id = "test2",
                name = "mama quest...",
                tasks = listOf(
                    QuestTask(0, "ДОМА", Coordinates(latitude=51.3268165, longitude=12.335707), "ты дома )", 50f),
                    QuestTask(1, "РОССМАН", Coordinates(51.32951639491752, 12.335894998487160), "иди к россману", 50f),
                    QuestTask(2, "ДОМА", Coordinates(latitude=51.3268165, longitude=12.335707), "снова домой", 50f),
                    QuestTask(3, "РОССМАН", Coordinates(51.32951639491752, 12.33589499848716), "иди к россману", 50f),
                    QuestTask(4, "ДОМА", Coordinates(latitude=51.3268165, longitude=12.335707), "ты дома )", 50f),
                    QuestTask(5, "РОССМАН", Coordinates(51.32951639491752, 12.335894998487160), "иди к россману", 50f),
                    QuestTask(6, "ДОМА", Coordinates(latitude=51.3268165, longitude=12.335707), "снова домой", 50f),
                    QuestTask(7, "РОССМАН", Coordinates(51.32951639491752, 12.33589499848716), "иди к россману", 50f),
                    QuestTask(8, "ДОМА", Coordinates(latitude=51.3268165, longitude=12.335707), "ты дома )", 50f),
                    QuestTask(9, "РОССМАН", Coordinates(51.32951639491752, 12.335894998487160), "иди к россману", 50f),
                    QuestTask(10, "ДОМА", Coordinates(latitude=51.3268165, longitude=12.335707), "снова домой", 50f),
                    QuestTask(11, "РОССМАН", Coordinates(51.32951639491752, 12.33589499848716), "иди к россману", 50f),
                    QuestTask(12, "ДОМА", Coordinates(latitude=51.3268165, longitude=12.335707), "ты дома )", 50f),
                    QuestTask(13, "РОССМАН", Coordinates(51.32951639491752, 12.335894998487160), "иди к россману", 50f),
                    QuestTask(14, "ДОМА", Coordinates(latitude=51.3268165, longitude=12.335707), "снова домой", 50f),
                    QuestTask(15, "РОССМАН", Coordinates(51.32951639491752, 12.33589499848716), "иди к россману", 50f),
                ),
                startLocation = Coordinates(latitude=51.3268165, longitude=12.335707)
            )
        )

        questRepository.addQuest(
            Quest(
                id = "test3",
                name = "another quest",
                tasks = listOf(
                    QuestTask(0, "task 1", Coordinates(latitude=51.3268165, longitude=12.335707), "home", 50f),
                    QuestTask(1, "task 2", Coordinates(51.32821071521285, 12.332517013700437), "basketball", 50f),
                    QuestTask(2, "task 3", Coordinates(51.3280961944985, 12.334084616176536), "spinny thingy", 10f),
                    QuestTask(3, "task 4", Coordinates(latitude=51.3268165, longitude=12.335707), "home", 50f),

                    ),
                startLocation = Coordinates(latitude=51.3268165, longitude=12.335707)
            )
        )
    }
}