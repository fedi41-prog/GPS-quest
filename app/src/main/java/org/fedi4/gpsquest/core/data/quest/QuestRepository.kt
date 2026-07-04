package org.fedi4.gpsquest.core.data.quest

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.fedi4.gpsquest.core.data.models.Coordinates
import org.fedi4.gpsquest.core.data.models.Quest
import org.fedi4.gpsquest.core.data.models.QuestRun
import org.fedi4.gpsquest.core.data.models.QuestTask

class QuestRepository(private val storage: QuestStorage) {

    private val _quests = MutableStateFlow<List<Quest>>(emptyList())
    val quests = _quests.asStateFlow()

    private val _questRun = MutableStateFlow<QuestRun?>(null)
    val questRun = _questRun.asStateFlow()

    init {
        _quests.update { storage.loadAllQuests() }
        Log.d("QuestRepository", "Loaded ${_quests.value.size} quests from disk")
    }

    fun addQuest(quest: Quest) {
        storage.saveQuest(quest)
        _quests.update { it + quest }
    }

    fun removeQuest(quest: Quest) {
        storage.deleteQuest(quest)
        _quests.update { list -> list.filterNot { it.name == quest.name } }
    }

    fun loadQuest(id: String) {
        // look up in _quests.value by id and set as active quest / questRun, etc.
    }

    fun addTestQuest() {
        addQuest(
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

        )
        addQuest(
            Quest(
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

        addQuest(
            Quest(
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

    fun updateRun(state: QuestRun) {
        _questRun.value = state
    }
}