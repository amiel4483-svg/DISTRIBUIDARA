package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF90CAF9),
    onPrimary = Color(0xFF0D47A1),
    primaryContainer = DaniisaBlueDark,
    onPrimaryContainer = Color(0xFFD1E4FF),
    secondary = Color(0xFF81C784),
    onSecondary = Color(0xFF003915),
    error = DaniisaRed,
    background = Color(0xFF111318),
    surface = Color(0xFF1A1C20),
    onBackground = Color(0xFFE2E2E6),
    onSurface = Color(0xFFE2E2E6),
)

private val LightColorScheme = lightColorScheme(
    primary = DaniisaBlue,
    onPrimary = Color.White,
    primaryContainer = DaniisaBlueLight,
    onPrimaryContainer = DaniisaBlueDark,
    secondary = DaniisaGreen,
    onSecondary = Color.White,
    secondaryContainer = DaniisaGreenLight,
    onSecondaryContainer = DaniisaGreenDark,
    error = DaniisaRed,
    background = SurfaceLight,
    surface = Color.White,
    onBackground = DaniisaTextPrimary,
    onSurface = DaniisaTextPrimary,
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep branded Daniisa blue by default
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
