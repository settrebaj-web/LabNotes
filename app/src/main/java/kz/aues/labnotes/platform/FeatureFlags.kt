package kz.aues.labnotes.platform

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kz.aues.labnotes.BuildConfig

/**
 * Remote Config арқылы feature flags және kill switch (Тапсырма 14).
 * Сервер мәндері: Firebase Console → Remote Config.
 */
class FeatureFlags(context: Context) {
    private val remote: FirebaseRemoteConfig? =
        if (FirebaseApp.getApps(context).isEmpty()) {
            null
        } else {
            FirebaseRemoteConfig.getInstance().apply {
                setConfigSettingsAsync(
                    FirebaseRemoteConfigSettings
                        .Builder()
                        .setMinimumFetchIntervalInSeconds(if (BuildConfig.DEBUG) 0 else FETCH_INTERVAL_SECONDS)
                        .build()
                )
                setDefaultsAsync(DEFAULTS)
            }
        }

    /** Іздеу функциясы. Апат кезінде консольден false қойып, бірден өшіруге болады. */
    val searchEnabled: Boolean
        get() = remote?.getBoolean(KEY_SEARCH_ENABLED) ?: DEFAULT_SEARCH_ENABLED

    /** Осы versionCode-тан төмен нұсқалар міндетті түрде жаңартылады (in-app update IMMEDIATE). */
    val minSupportedVersionCode: Long
        get() = remote?.getLong(KEY_MIN_VERSION_CODE) ?: 0L

    fun fetch(onDone: () -> Unit) {
        val rc = remote
        if (rc == null) {
            onDone()
            return
        }
        rc.fetchAndActivate().addOnCompleteListener { onDone() }
    }

    companion object {
        const val KEY_SEARCH_ENABLED = "search_enabled"
        const val KEY_MIN_VERSION_CODE = "min_supported_version_code"
        private const val DEFAULT_SEARCH_ENABLED = true
        private const val FETCH_INTERVAL_SECONDS = 3600L
        private val DEFAULTS =
            mapOf<String, Any>(
                KEY_SEARCH_ENABLED to DEFAULT_SEARCH_ENABLED,
                KEY_MIN_VERSION_CODE to 0L
            )
    }
}
