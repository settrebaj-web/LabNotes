package kz.aues.labnotes.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

/** Жазбаларды құрылғыда SharedPreferences ішінде сақтайды. Серверге ештеңе жіберілмейді. */
class NoteRepository(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun load(): List<Note> {
        val raw = prefs.getString(KEY_NOTES, null) ?: return emptyList()
        val array = JSONArray(raw)
        val result = ArrayList<Note>(array.length())
        for (i in 0 until array.length()) {
            val o = array.getJSONObject(i)
            result += Note(o.getLong("id"), o.getString("text"), o.getLong("createdAt"))
        }
        return NoteRules.sortNewestFirst(result)
    }

    fun add(text: String): List<Note>? {
        val clean = NoteRules.normalize(text) ?: return null
        val now = System.currentTimeMillis()
        val updated = load() + Note(id = now, text = clean, createdAt = now)
        save(updated)
        return NoteRules.sortNewestFirst(updated)
    }

    fun delete(id: Long): List<Note> {
        val updated = load().filterNot { it.id == id }
        save(updated)
        return updated
    }

    private fun save(notes: List<Note>) {
        val array = JSONArray()
        notes.forEach {
            array.put(
                JSONObject()
                    .put("id", it.id)
                    .put("text", it.text)
                    .put("createdAt", it.createdAt)
            )
        }
        prefs.edit().putString(KEY_NOTES, array.toString()).apply()
    }

    private companion object {
        const val PREFS_NAME = "notes"
        const val KEY_NOTES = "notes_json"
    }
}
