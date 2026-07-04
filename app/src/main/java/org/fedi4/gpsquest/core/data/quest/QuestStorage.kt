package org.fedi4.gpsquest.core.data.quest

import android.content.Context
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.fedi4.gpsquest.core.data.models.Quest
import java.io.File

class QuestStorage(private val context: Context) {

    private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }
    private val questsDir get() = File(context.filesDir, "quests").apply { mkdirs() }

    fun saveQuest(quest: Quest) {
        val file = File(questsDir, "${quest.name.hashCode()}.json") // better: a stable id field
        file.writeText(json.encodeToString(quest))
    }

    fun loadAllQuests(): List<Quest> =
        questsDir.listFiles { f -> f.extension == "json" }
            ?.mapNotNull { f ->
                runCatching { json.decodeFromString<Quest>(f.readText()) }.getOrNull()
            } ?: emptyList()

    fun deleteQuest(quest: Quest) {
        File(questsDir, "${quest.name.hashCode()}.json").delete()
    }
}