package com.nparmenov.wiam_demo

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF005AC1),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD6E3FF),
    onPrimaryContainer = Color(0xFF001B3F),

    secondary = Color(0xFF006A60),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF72F7E6),
    onSecondaryContainer = Color(0xFF00201C),

    tertiary = Color(0xFF7B4EFF),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFE5DEFF),
    onTertiaryContainer = Color(0xFF25005A),

    background = Color(0xFFFFFBFF),
    onBackground = Color(0xFF1A1B1F),
    surface = Color(0xFFF7F2FA),
    onSurface = Color(0xFF1A1B1F),

    surfaceVariant = Color(0xFFE3E1EC),
    onSurfaceVariant = Color(0xFF46464F),

    error = Color(0xFFB3261E),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B),

    outline = Color(0xFF777680)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF8CC8FF),
    onPrimary = Color(0xFF00315F),
    primaryContainer = Color(0xFF00488D),
    onPrimaryContainer = Color(0xFFD6E3FF),

    secondary = Color(0xFF4DD9C7),
    onSecondary = Color(0xFF003731),
    secondaryContainer = Color(0xFF005049),
    onSecondaryContainer = Color(0xFF72F7E6),

    tertiary = Color(0xFFD0BCFF),
    onTertiary = Color(0xFF3A1B72),
    tertiaryContainer = Color(0xFF53368B),
    onTertiaryContainer = Color(0xFFE5DEFF),

    background = Color(0xFF0B0F14),
    onBackground = Color(0xFFE3E2E8),
    surface = Color(0xFF111823),
    onSurface = Color(0xFFE3E2E8),

    surfaceVariant = Color(0xFF1B2635),
    onSurfaceVariant = Color(0xFFC6C5D0),

    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFF9DEDC),

    outline = Color(0xFF90909A)
)

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()

    MaterialTheme(
        colorScheme = if (isDarkTheme) DarkColorScheme else LightColorScheme,
        content = content
    )
}
