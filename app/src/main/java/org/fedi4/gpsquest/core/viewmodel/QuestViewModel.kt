package org.fedi4.gpsquest.core.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import org.fedi4.gpsquest.core.data.models.Coordinates
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

    fun currentTaskProgress(distanceToNextTask: Float): Float {
        """return progress of current task as float between 0 and 1"""

        if (questRun.value == null) return 0f
        if (questRun.value?.progress == null) return 0f

        val progress = questRun.value?.progress ?: return 0f
        val quest = questRun.value?.quest ?: return 0f

        val task = quest.tasks[progress]

        if (progress == 0) {
            return quest.startLocation.distanceTo(task.coordinates).toFloat() / distanceToNextTask
        }
        return quest.tasks[progress-1].coordinates.distanceTo(task.coordinates).toFloat() / distanceToNextTask
    }

    companion object {

        @RequiresApi(Build.VERSION_CODES.O)
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