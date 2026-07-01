package org.fedi4.gpsquest.core.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import org.fedi4.gpsquest.core.data.gps.GPSState
import org.fedi4.gpsquest.core.data.quest.QuestEngine
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import org.fedi4.gpsquest.core.GPSQuestApplication
import org.fedi4.gpsquest.core.data.gps.LocationRepository
import org.fedi4.gpsquest.core.data.models.Quest
import org.fedi4.gpsquest.core.data.quest.QuestRepository

class QuestViewModel(
    private val questRepository: QuestRepository,
    private val locationRepository: LocationRepository,

    private val engine: QuestEngine
) : ViewModel() {
    val gpsState = locationRepository.state

    val questRun = questRepository.questRun
    val quests = questRepository.quests

    init {
        locationRepository.start()
        Log.d("QuestViewModel", "QuestViewModel created")
    }

    fun refreshGPS() {
        locationRepository.start()
    }

    override fun onCleared() {
        locationRepository.stop()
    }



    init {
        observeGPS()
    }

    fun startQuest(quest: Quest){
        engine.startQuest(quest)
//        app.isRunningQuest.value = true
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