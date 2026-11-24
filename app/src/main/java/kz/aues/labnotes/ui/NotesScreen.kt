package kz.aues.labnotes.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.DateFormat
import java.util.Date
import kz.aues.labnotes.data.Note
import kz.aues.labnotes.data.NoteRepository
import kz.aues.labnotes.data.NoteRules
import kz.aues.labnotes.platform.Telemetry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(repository: NoteRepository, searchEnabled: Boolean, showDebugTools: Boolean, footer: String) {
    var notes by remember { mutableStateOf(repository.load()) }
    var input by remember { mutableStateOf("") }
    var query by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Scaffold(topBar = { TopAppBar(title = { Text("LabNotes") }) }) { padding ->
        Column(
            modifier =
            Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = input,
                    onValueChange = {
                        input = it
                        error = null
                    },
                    label = { Text("Жаңа жазба") },
                    isError = error != null,
                    supportingText = { error?.let { Text(it) } },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.padding(4.dp))
                Button(onClick = {
                    val updated = repository.add(input)
                    if (updated == null) {
                        error = "Бос жазбаны сақтауға болмайды"
                    } else {
                        notes = updated
                        input = ""
                        Telemetry.event("note_added")
                    }
                }) { Text("Қосу") }
            }

            if (searchEnabled) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Іздеу") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            val visible = if (searchEnabled) NoteRules.search(notes, query) else notes
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(visible, key = { it.id }) { note ->
                    NoteCard(note) {
                        notes = repository.delete(note.id)
                        Telemetry.event("note_deleted")
                    }
                }
            }

            if (showDebugTools) DebugTools()
            Text(footer, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}

@Composable
private fun NoteCard(note: Note, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(note.text, style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(4.dp))
                Text(
                    DateFormat.getDateTimeInstance().format(Date(note.createdAt)),
                    style = MaterialTheme.typography.labelSmall
                )
            }
            TextButton(onClick = onDelete) { Text("Жою") }
        }
    }
}

/** Тек debug/staging жинақтарында көрінеді (BuildConfig.SHOW_DEBUG_TOOLS). */
@Composable
private fun DebugTools() {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 8.dp)) {
        OutlinedButton(onClick = { Telemetry.nonFatal(IllegalStateException("Test non-fatal")) }) {
            Text("Non-fatal")
        }
        Button(onClick = { triggerTestCrash() }) { Text("Тест крэш") }
    }
}

@Suppress("TooGenericExceptionThrown")
private fun triggerTestCrash(): Nothing {
    Telemetry.breadcrumb("Пайдаланушы тест крэшті басты")
    throw RuntimeException("Test Crash — LabNotes (Тапсырма 13)")
}
