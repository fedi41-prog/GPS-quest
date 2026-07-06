package org.fedi4.gpsquest.core.data.quest

import android.util.Log
import org.fedi4.gpsquest.core.data.gps.LocationState
import org.fedi4.gpsquest.core.data.models.Coordinates
import org.fedi4.gpsquest.core.data.models.Quest
import org.fedi4.gpsquest.core.data.models.QuestRun
import org.fedi4.gpsquest.core.data.models.QuestTask

class QuestEngine(
    private val repository: QuestRepository,
    private val onTaskCompleted: () -> Unit = {}
) {

    val questRun = repository.questRun

    var currentLocation: LocationState? = null

    fun startQuest(quest: Quest) {
        repository.updateRun(
            QuestRun(
                quest = quest,
                progress = 0,
                distanceToNextTask = 0.0,
                startCoordinates = Coordinates(0.0, 0.0)
            )
        )
        Log.d("QuestEngine", "Quest started: ${quest.name}")
    }

    fun currentTask(): QuestTask? {
        if (questRun.value?.finished == true) return null
        return questRun.value?.quest?.tasks?.get(questRun.value?.progress ?: return null) as QuestTask?
    }

    fun updateLocation(location: LocationState) {
        currentLocation = location
        tick()
    }

    fun tick() {
        currentLocation?.let { location ->
            currentTask()?.let { task ->
                repository.questRun.value?.let { run ->

                    val distance = location.coordinates.distanceTo(task.coordinates)

                    Log.d("QuestEngine", "Player location: ${location.coordinates}")
                    Log.d("QuestEngine", "Distance to task: $distance")

                    repository.updateRun(
                        run.copy(
                            distanceToNextTask = distance,
                            startCoordinates = if (run.progress == 0) location.coordinates else run.startCoordinates
                        )
                    )

                    if (distance <= task.radius) {
                        next()
                    }

                }
            }
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

        onTaskCompleted()
        tick()
    }
}