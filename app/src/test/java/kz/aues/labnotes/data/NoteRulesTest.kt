package kz.aues.labnotes.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class NoteRulesTest {
    private val notes =
        listOf(
            Note(1, "Купить молоко", 100),
            Note(2, "Лабораторная работа 8", 300),
            Note(3, "Емтиханға дайындалу", 200)
        )

    @Test
    fun `blank input is rejected`() {
        assertNull(NoteRules.normalize(""))
        assertNull(NoteRules.normalize("   \n\t"))
    }

    @Test
    fun `input is trimmed`() {
        assertEquals("сәлем", NoteRules.normalize("  сәлем  "))
    }

    @Test
    fun `long input is truncated`() {
        val long = "a".repeat(NoteRules.MAX_LENGTH + 50)
        assertEquals(NoteRules.MAX_LENGTH, NoteRules.normalize(long)!!.length)
    }

    @Test
    fun `search is case insensitive`() {
        assertEquals(listOf(2L), NoteRules.search(notes, "лабораторная").map { it.id })
    }

    @Test
    fun `empty query returns all notes`() {
        assertEquals(notes, NoteRules.search(notes, "  "))
    }

    @Test
    fun `search with no matches returns empty list`() {
        assertEquals(emptyList<Note>(), NoteRules.search(notes, "xyz"))
    }

    @Test
    fun `newest notes go first`() {
        assertEquals(listOf(2L, 3L, 1L), NoteRules.sortNewestFirst(notes).map { it.id })
    }
}
