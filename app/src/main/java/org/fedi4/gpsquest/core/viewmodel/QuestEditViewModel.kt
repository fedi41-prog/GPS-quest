package org.fedi4.gpsquest.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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
        _draft.update { it?.copy(tasks = it.tasks + task) }
    }

    fun newTask() {
        _draft.value?.let { q ->
            addTask(
                QuestTask(
                    q.tasks.size,
                    "New Task :D",
                    Coordinates(0.0, 0.0),
                    "task description...",
                    40f
                )
            )
        }
    }


    fun removeTask(idx: Int) {
        _draft.update { it?.copy(tasks = it.tasks.filterNot { t -> t.idx == idx }) }
    }

    fun save() {
        _draft.value?.let { repo.updateQuest(it) }
    }

    fun discard() {
        _draft.value = repo.quests.value.find { it.id == questId }
    }


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