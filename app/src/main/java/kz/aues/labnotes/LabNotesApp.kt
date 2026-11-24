package kz.aues.labnotes

import android.app.Application
import kz.aues.labnotes.platform.Telemetry

class LabNotesApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Telemetry.init(this)
    }
}
