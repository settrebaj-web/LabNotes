package kz.aues.labnotes.platform

import android.app.Activity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import kz.aues.labnotes.BuildConfig

/**
 * Google Play In-App Updates — міндетті (IMMEDIATE) жаңарту (Тапсырма 14).
 * Тек Play-ден орнатылған нұсқада жұмыс істейді; басқа жағдайда үнсіз өтеді.
 */
class UpdateChecker(activity: Activity, private val launcher: ActivityResultLauncher<IntentSenderRequest>) {
    private val manager = AppUpdateManagerFactory.create(activity)

    fun checkForcedUpdate(minSupportedVersionCode: Long) {
        if (BuildConfig.VERSION_CODE >= minSupportedVersionCode) return
        Telemetry.breadcrumb("forced update required: min=$minSupportedVersionCode")
        manager.appUpdateInfo
            .addOnSuccessListener { info ->
                val available = info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                if (available && info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) start(info)
            }.addOnFailureListener { Telemetry.breadcrumb("in-app update unavailable: ${it.message}") }
    }

    /** IMMEDIATE жаңарту үзіліп қалса, onResume-да қайта жалғастырамыз. */
    fun resumeIfInProgress() {
        manager.appUpdateInfo.addOnSuccessListener { info ->
            if (info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) start(info)
        }
    }

    private fun start(info: AppUpdateInfo) {
        manager.startUpdateFlowForResult(
            info,
            launcher,
            AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
        )
    }
}
