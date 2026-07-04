package org.fedi4.gpsquest.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.fedi4.gpsquest.core.GPSQuestApplication
import org.fedi4.gpsquest.core.data.models.Quest
import org.fedi4.gpsquest.core.data.models.QuestTask
import org.fedi4.gpsquest.core.data.quest.QuestRepository

class QuestEditViewModel(
    private val repo: QuestRepository,
    private val questId: String
) : ViewModel() {

    val quest: StateFlow<Quest?> = repo.quests
        .map { it.find { q -> q.id == questId } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun onTaskEdited(task: QuestTask, newName: String, newDesc: String, newRadius: Float) {
        repo.updateTask(questId, task.copy(name = newName, description = newDesc, radius = newRadius))
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