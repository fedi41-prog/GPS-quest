package org.fedi4.gpsquest.core.data.quest

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.fedi4.gpsquest.core.data.models.Coordinates
import org.fedi4.gpsquest.core.data.models.Quest
import org.fedi4.gpsquest.core.data.models.QuestRun
import org.fedi4.gpsquest.core.data.models.QuestTask

class QuestRepository {

    private val _quests = MutableStateFlow<List<Quest>>(emptyList())
    val quests = _quests.asStateFlow()

    private val _questRun = MutableStateFlow<QuestRun?>(null)
    val questRun = _questRun.asStateFlow()

    init {


        _quests.update { quests ->


            return@update quests + listOf<Quest>(
                Quest(
                    name = "cool quest bruh",
                    tasks = listOf(
                        QuestTask(0, "abracadabra", Coordinates(latitude=51.3268165, longitude=12.335707), "first task", 100f),
                        QuestTask(1, "cadabrababra", Coordinates(latitude=51.3268165, longitude=12.335707), "firdawdwst task", 100f),
                        QuestTask(2, "tralalerotralala", Coordinates(latitude=51.3268165, longitude=12.335707), "first taddsk", 100f),
                        QuestTask(3, "tungtungtungsahur ", Coordinates(latitude=51.3268165, longitude=12.335707), "first dwda task", 100f)
                    ),
                    startLocation = Coordinates(latitude=51.3268165, longitude=12.335707)
                ),
                Quest(
                    name = "mama quest...",
                    tasks = listOf(
                        QuestTask(0, "ДОМА", Coordinates(latitude=51.3268165, longitude=12.335707), "ты дома )", 50f),
                        QuestTask(1, "РОССМАН", Coordinates(51.32951639491752, 12.335894998487160), "иди к россману", 50f),
                        QuestTask(2, "ДОМА", Coordinates(latitude=51.3268165, longitude=12.335707), "снова домой", 50f),
                        QuestTask(1, "РОССМАН", Coordinates(51.32951639491752, 12.33589499848716), "иди к россману", 50f),
                    ),
                    startLocation = Coordinates(latitude=51.3268165, longitude=12.335707)
                )
            )
        }


        Log.d("QuestRepository", "QuestRepository created")
        Log.d("QuestRepository", "Quests: ${_quests.value}")

    }

    fun loadQuest(id: String) {}


    fun updateRun(state: QuestRun) {
        _questRun.value = state
    }
}