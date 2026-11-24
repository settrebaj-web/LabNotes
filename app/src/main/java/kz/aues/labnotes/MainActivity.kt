package kz.aues.labnotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
        val footer = "v${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE}) · ${BuildConfig.API_BASE_URL}"

        setContent {
            LabNotesTheme {
                NotesScreen(
                    repository = repository,
                    searchEnabled = searchEnabled,
                    showDebugTools = BuildConfig.SHOW_DEBUG_TOOLS,
                    footer = footer
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateChecker.resumeIfInProgress()
    }
}
