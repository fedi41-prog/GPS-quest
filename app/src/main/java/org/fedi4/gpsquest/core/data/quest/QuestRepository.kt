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
            quests + Quest(
                name = "cool quest bruh",
                tasks = listOf(
                    QuestTask(0, "abracadabra", Coordinates(1.0, 1.0), "first task", 100f),
                    QuestTask(1, "cadabrababra", Coordinates(1.0, 1.0), "firdawdwst task", 100f),
                    QuestTask(2, "tralalerotralala", Coordinates(1.0, 1.0), "first taddsk", 100f),
                    QuestTask(3, "tungtungtungsahur ", Coordinates(1.0, 1.0), "first dwda task", 100f)
                )
            )
        }


        Log.d("QuestRepository", "QuestRepository created")
        Log.d("QuestRepository", "Quests: ${_quests.value}")

    }

    fun loadQuest(id: String) {}

    fun saveProgress() {}

    fun resetQuest() {}
    fun updateRun(state: QuestRun) {
        _questRun.value = state
    }
}