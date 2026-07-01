package org.fedi4.gpsquest.core.data.quest

import android.util.Log
import org.fedi4.gpsquest.core.data.gps.LocationState
import org.fedi4.gpsquest.core.data.models.Quest
import org.fedi4.gpsquest.core.data.models.QuestRun
import org.fedi4.gpsquest.core.data.models.QuestTask

class QuestEngine(
    private val repository: QuestRepository
) {


    val questRun = repository.questRun


    fun startQuest(quest: Quest) {
        repository.updateRun(
            QuestRun(
                quest = quest,
                progress = 0
            )
        )
        Log.d("QuestEngine", "Quest started: ${quest.name}")
        Log.d("QuestEngine", "Quest progress: ${questRun.value?.progress}")
        Log.d("QuestEngine", "Quest tasks: ${questRun.value?.quest?.tasks}")
    }

    fun currentTask(): QuestTask? {
        return questRun.value?.quest?.tasks?.get(questRun.value?.progress ?: return null) as QuestTask?
    }
    fun updateLocation(location: LocationState) {
        val task = currentTask()

        val distance = 10000f

        if(distance <= (task?.radius ?: return)){
            next()
        }
    }

    fun next() {
        val run = repository.questRun.value ?: return

        repository.updateRun(
            run.copy(
                progress = run.progress + 1
            )
        )
        Log.d("QuestEngine", "Quest progress: ${run.progress}")
    }
}