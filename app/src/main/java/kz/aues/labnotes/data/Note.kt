package kz.aues.labnotes.data

data class Note(val id: Long, val text: String, val createdAt: Long)

/** Таза Kotlin логикасы — unit-тесттермен тексеріледі (Тапсырма 10). */
object NoteRules {
    const val MAX_LENGTH = 500

    /**
     * Енгізілген мәтінді тазалайды. Бос немесе тек бос орындардан тұратын жазба
     * сақталмайды (1.1.0 нұсқасындағы түзету).
     */
    fun normalize(input: String): String? {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) return null
        return trimmed.take(MAX_LENGTH)
    }

    /** Іздеу (1.2.x функциясы). Бос сұраныс — барлық жазба. */
    fun search(notes: List<Note>, query: String): List<Note> {
        val q = query.trim()
        if (q.isEmpty()) return notes
        return notes.filter { it.text.contains(q, ignoreCase = true) }
    }

    /** Жаңалары жоғарыда. */
    fun sortNewestFirst(notes: List<Note>): List<Note> = notes.sortedByDescending { it.createdAt }
}
