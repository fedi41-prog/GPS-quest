package org.fedi4.gpsquest.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.fedi4.gpsquest.core.GPSQuestApplication
import org.fedi4.gpsquest.core.data.models.Coordinates
import org.fedi4.gpsquest.core.data.models.Quest
import org.fedi4.gpsquest.core.data.models.QuestTask
import org.fedi4.gpsquest.core.data.quest.QuestRepository

class QuestEditViewModel(
    private val repo: QuestRepository,
    private val questId: String
) : ViewModel() {

    private val _draft = MutableStateFlow<Quest?>(null)
    val draft: StateFlow<Quest?> = _draft.asStateFlow()

    // wird true, nachdem die Quest gelöscht wurde -> UI kann darauf reagieren und z.B. zurück navigieren
    private val _deleted = MutableStateFlow(false)
    val deleted: StateFlow<Boolean> = _deleted.asStateFlow()

    init {
        viewModelScope.launch {
            _draft.value = repo.quests.value.find { it.id == questId }
        }
    }

    fun onTaskEdited(task: QuestTask) {
        _draft.update { q ->
            q?.copy(tasks = q.tasks.map { if (it.idx == task.idx) task else it })
        }
    }

    fun addTask(task: QuestTask) {
        _draft.update { q ->
            q?.copy(tasks = (q.tasks + task).reindexed())
        }
    }

    fun updateQuest(quest: Quest) {
        _draft.update { q -> quest }
    }

    fun updateQuestName(name: String) {
        _draft.update { q -> q?.copy(
            name = name
        ) }
    }


    fun newTask() {
        _draft.value?.let { q ->
            addTask(
                QuestTask(
                    q.tasks.size,
                    Coordinates(0.0, 0.0),
                    "",
                    40f
                )
            )
        }
    }

    fun removeTask(idx: Int) {
        _draft.update { q ->
            q?.copy(
                tasks = q.tasks
                    .filterNot { it.idx == idx }
                    .reindexed() // idx nach dem Löschen wieder lückenlos machen
            )
        }
    }

    fun reorderTasks(newOrder: List<QuestTask>) {
        _draft.update { q ->
            q?.copy(tasks = newOrder.reindexed())
        }
    }

    fun save() {
        _draft.value?.let { repo.updateQuest(it) }
    }

    fun discard() {
        _draft.value = repo.quests.value.find { it.id == questId }
    }

    fun deleteQuest() {
        _draft.value?.let { repo.removeQuest(it) }
        _deleted.value = true
    }

    // Hilfsfunktion: setzt idx = Listenposition für alle Tasks
    private fun List<QuestTask>.reindexed(): List<QuestTask> =
        mapIndexed { i, t -> t.copy(idx = i) }

    companion object {

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {

                val app = this[APPLICATION_KEY] as GPSQuestApplication
                val savedStateHandle = createSavedStateHandle()

                QuestEditViewModel(
                    app.questRepository,
                    savedStateHandle.get<String>("questId")!!
                )
            }
        }
    }
}