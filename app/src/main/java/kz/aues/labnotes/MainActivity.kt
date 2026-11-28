package kz.aues.labnotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kz.aues.labnotes.data.NoteRepository
import kz.aues.labnotes.platform.FeatureFlags
import kz.aues.labnotes.platform.Telemetry
import kz.aues.labnotes.platform.UpdateChecker
import kz.aues.labnotes.ui.LabNotesTheme
import kz.aues.labnotes.ui.NotesScreen

class MainActivity : ComponentActivity() {
    private lateinit var updateChecker: UpdateChecker
    private var searchEnabled by mutableStateOf(true)
    private var showPrivacyNotice by mutableStateOf(false)

    private val updateLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode != RESULT_OK) {
                Telemetry.event("in_app_update_declined")
                // IMMEDIATE жаңартудан бас тартса — қосымшаны жабамыз
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = NoteRepository(this)
        val flags = FeatureFlags(this)
        searchEnabled = flags.searchEnabled
        updateChecker = UpdateChecker(this, updateLauncher)

        flags.fetch {
            searchEnabled = flags.searchEnabled
            updateChecker.checkForcedUpdate(flags.minSupportedVersionCode)
        }
        Telemetry.event("app_open", mapOf("version" to BuildConfig.VERSION_NAME))
        // 1.1.0: API адресі тек debug/staging-те көрсетіледі (релизде ішкі ақпарат көрінбейді)
        val footer =
            buildString {
                append("v${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})")
                if (BuildConfig.SHOW_DEBUG_TOOLS) append(" · ${BuildConfig.API_BASE_URL}")
            }

        // 1.1.0: алғашқы іске қосуда құпиялылық туралы хабарлама
        val prefs = getSharedPreferences(PREFS_APP, MODE_PRIVATE)
        showPrivacyNotice = !prefs.getBoolean(KEY_PRIVACY_ACK, false)

        setContent {
            LabNotesTheme {
                NotesScreen(
                    repository = repository,
                    searchEnabled = searchEnabled,
                    showDebugTools = BuildConfig.SHOW_DEBUG_TOOLS,
                    footer = footer
                )
                if (showPrivacyNotice) {
                    PrivacyNoticeDialog(onAccept = {
                        prefs.edit().putBoolean(KEY_PRIVACY_ACK, true).apply()
                        showPrivacyNotice = false
                        Telemetry.event("privacy_notice_accepted")
                    })
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateChecker.resumeIfInProgress()
    }

    private companion object {
        const val PREFS_APP = "app"
        const val KEY_PRIVACY_ACK = "privacy_ack"
    }
}

@Composable
private fun PrivacyNoticeDialog(onAccept: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text("Құпиялылық") },
        text = {
            Text(
                "Жазбаларыңыз тек осы құрылғыда сақталады. Қосымша тұрақтылықты жақсарту үшін " +
                    "анонимді крэш-есептер мен жалпы статистиканы жібереді. Толығырақ: " +
                    "settrebaj-web.github.io/LabNotes/privacy-policy.html"
            )
        },
        confirmButton = { TextButton(onClick = onAccept) { Text("Түсінікті") } }
    )
}
