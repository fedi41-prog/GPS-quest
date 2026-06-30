package org.fedi4.gpsquest.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.fedi4.gpsquest.core.data.gps.GPSState
import org.fedi4.gpsquest.core.data.quest.QuestEngine
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import org.fedi4.gpsquest.core.GPSQuestApplication
import org.fedi4.gpsquest.core.data.gps.LocationRepository
import org.fedi4.gpsquest.core.data.models.QuestState
import org.fedi4.gpsquest.core.data.models.TaskState
import org.fedi4.gpsquest.core.data.quest.QuestRepository

class QuestViewModel(
    private val questRepository: QuestRepository,
    private val locationRepository: LocationRepository,

    private val engine: QuestEngine
) : ViewModel() {
    val gpsState = locationRepository.state

    init {
        locationRepository.start()
    }

    fun refreshGPS() {
        locationRepository.start()
    }

    override fun onCleared() {
        locationRepository.stop()
    }

    private val _questState = QuestState( // autocomplete cooked
        quest = questRepository.currentQuest.value,
        questName =questRepository.currentQuest.value?.name ?: "",
        tasks = questRepository.currentQuest.value?.tasks?.map {
            TaskState(
                task = it,
                taskName = it.name,
                taskDescription = it.description,
                finished = false
            )
        } ?: emptyList(),
        progress = 0,
        finished = false
    )
    val questState: MutableStateFlow<QuestState> = MutableStateFlow(_questState)

    init {
        observeGPS()
    }

    private fun observeGPS() {
        viewModelScope.launch {
            gpsState.collect {
                if(it is GPSState.Ready){
                    engine.updateLocation(it.location)
                }
            }
        }
    }

    fun next(){

        engine.next()

    }


    companion object {

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {

                val app = this[APPLICATION_KEY] as GPSQuestApplication

                QuestViewModel(
                    app.questRepository,
                    app.locationRepository,
                    app.questEngine
                )
            }
        }
    }
}