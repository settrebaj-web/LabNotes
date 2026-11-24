package kz.aues.labnotes.platform

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kz.aues.labnotes.BuildConfig

/**
 * Crashlytics + Analytics ораушысы (Тапсырма 13).
 * google-services.json жоқ болса, барлық шақыру үнсіз еленбейді.
 */
object Telemetry {
    private const val TAG = "LabNotes"
    private var analytics: FirebaseAnalytics? = null
    private var crashlytics: FirebaseCrashlytics? = null

    fun init(context: Context) {
        if (FirebaseApp.getApps(context).isEmpty()) {
            Log.d(TAG, "Firebase бапталмаған — телеметрия өшірулі")
            return
        }
        analytics = FirebaseAnalytics.getInstance(context)
        crashlytics =
            FirebaseCrashlytics.getInstance().apply {
                // debug жинағынан крэштер консольді ластамасын
                isCrashlyticsCollectionEnabled = !BuildConfig.DEBUG
                setCustomKey("build_type", BuildConfig.BUILD_TYPE)
                setCustomKey("version_code", BuildConfig.VERSION_CODE)
            }
    }

    fun event(name: String, params: Map<String, String> = emptyMap()) {
        Log.d(TAG, "event: $name $params")
        val bundle = Bundle()
        params.forEach { (k, v) -> bundle.putString(k, v) }
        analytics?.logEvent(name, bundle)
    }

    fun breadcrumb(message: String) {
        Log.d(TAG, message)
        crashlytics?.log(message)
    }

    fun nonFatal(error: Throwable) {
        crashlytics?.recordException(error)
    }
}
