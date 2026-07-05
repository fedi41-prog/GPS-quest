package org.fedi4.gpsquest.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.fedi4.gpsquest.MainActivity
import org.fedi4.gpsquest.core.data.models.Quest
import org.fedi4.gpsquest.core.data.quest.QuestEngine
import org.fedi4.gpsquest.core.data.quest.QuestRepository
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import org.fedi4.gpsquest.core.GPSQuestApplication

class HomeViewModel(
    private val questRepository: QuestRepository,
    private val engine: QuestEngine
) : ViewModel() {

    val quests = questRepository.quests

    fun startQuest(quest: Quest){
        engine.startQuest(quest)
//        app.isRunningQuest.value = true
    }
    fun editQuest(quest: Quest) {

    }
    fun createQuest(): Quest = questRepository.createQuest()
    companion object {

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {

                val app = this[APPLICATION_KEY] as GPSQuestApplication

                HomeViewModel(
                    app.questRepository,
                    app.questEngine
                )
            }
        }
    }

}