package kz.aues.labnotes.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Blue = Color(0xFF1565C0)
private val LightBlue = Color(0xFF90CAF9)

@Composable
fun LabNotesTheme(content: @Composable () -> Unit) {
    val scheme =
        if (isSystemInDarkTheme()) {
            darkColorScheme(primary = LightBlue)
        } else {
            lightColorScheme(primary = Blue)
        }
    MaterialTheme(colorScheme = scheme, content = content)
}
