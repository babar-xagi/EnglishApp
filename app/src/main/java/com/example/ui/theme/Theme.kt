package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = MintAccent,
    secondary = LimeSage,
    tertiary = GeoStreakOrange,
    background = DarkCanvas,
    surface = DarkPremiumSurface,
    onPrimary = DarkCanvas,
    onSecondary = DarkCanvas,
    onBackground = DarkTextLight,
    onSurface = DarkTextLight
)

private val LightColorScheme = lightColorScheme(
    primary = GeoPrimary,
    secondary = GeoPrimaryVariant,
    tertiary = GeoStreakOrange,
    background = LightBackground,
    surface = LightSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = SlateDark,
    onSurface = SlateDark
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
