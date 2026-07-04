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
        if (_quests.value.any { it.id == quest.id }) return
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


    fun updateTask(questId: String, updatedTask: QuestTask) {
        _quests.update { quests ->
            quests.map { quest ->
                if (quest.id != questId) return@map quest
                quest.copy(
                    tasks = quest.tasks.map { task ->
                        if (task.idx == updatedTask.idx) updatedTask else task
                    }
                )
            }
        }
    }

    fun addTask(questId: String, task: QuestTask) {
        _quests.update { quests ->
            quests.map { quest ->
                if (quest.id != questId) return@map quest
                quest.copy(tasks = quest.tasks + task)
            }
        }
    }

    fun removeTask(questId: String, taskIdx: Int) {
        _quests.update { quests ->
            quests.map { quest ->
                if (quest.id != questId) return@map quest
                quest.copy(tasks = quest.tasks.filterNot { it.idx == taskIdx })
            }
        }
    }

    fun reorderTasks(questId: String, newOrder: List<QuestTask>) {
        _quests.update { quests ->
            quests.map { quest ->
                if (quest.id != questId) return@map quest
                // re-index so idx always matches position, avoiding future duplicate-idx bugs
                quest.copy(tasks = newOrder.mapIndexed { i, t -> t.copy(idx = i) })
            }
        }
    }





    fun updateRun(state: QuestRun) {
        _questRun.value = state
    }
}