package org.fedi4.gpsquest.core.data.quest

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.fedi4.gpsquest.core.data.gps.LocationState
import org.fedi4.gpsquest.core.data.models.Quest
import org.fedi4.gpsquest.core.data.models.QuestTask

class QuestEngine(
    private val repository: QuestRepository
) {

    private val _currentTask =
        MutableStateFlow<QuestTask?>(null)
    val currentTask = _currentTask.asStateFlow()

    private val _progress =
        MutableStateFlow(0)
    val progress = _progress.asStateFlow()

    private val _distance =
        MutableStateFlow(0f)
    val distance = _distance.asStateFlow()

    private val _finished =
        MutableStateFlow(false)
    val finished = _finished.asStateFlow()


    fun startQuest(quest: Quest) {

        repository.currentQuest.value = quest

        _progress.value = 0

        _finished.value = false

        _currentTask.value = quest.tasks.first()

    }

    fun updateLocation(location: LocationState) {

        val task = _currentTask.value ?: return

//        if(step.type !is LocationStep)
//            return

        val distance = 10000f

        _distance.value = distance

        if(distance <= task.radius){
            next()
        }
    }

    fun next() {

        val quest =
            repository.currentQuest.value ?: return

        _progress.value++

        if(_progress.value >= quest.tasks.size){

            _finished.value = true

            return

        }

        _currentTask.value =
            quest.tasks[_progress.value]

    }
}