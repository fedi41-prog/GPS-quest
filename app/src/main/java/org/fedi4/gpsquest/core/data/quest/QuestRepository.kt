package org.fedi4.gpsquest.core.data.quest

import kotlinx.coroutines.flow.MutableStateFlow
import org.fedi4.gpsquest.core.data.models.Coordinates
import org.fedi4.gpsquest.core.data.models.Quest
import org.fedi4.gpsquest.core.data.models.QuestTask

class QuestRepository {

    val currentQuest = MutableStateFlow<Quest?>(null)

    init {
        currentQuest.value = Quest(
            name = "cool quest bruh",
            tasks = listOf(
                QuestTask("TASK 1", Coordinates(1.0, 1.0), "first task", 100f),
                QuestTask("TASK 2", Coordinates(1.0, 1.0), "firdawdwst task", 100f),
                QuestTask("TASK 3", Coordinates(1.0, 1.0), "first taddsk", 100f),
                QuestTask("TASK 4", Coordinates(1.0, 1.0), "first dwda task", 100f)
            )
        )
    }

    fun loadQuest(id: String) {}

    fun saveProgress() {}

    fun resetQuest() {}

}