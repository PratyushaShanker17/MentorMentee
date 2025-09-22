package com.x0Asian.MxM.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// New Purple Palette
val PurpleA = Color(0xFF6200EE) // Primary Light
val PurpleB = Color(0xFF7F39FB) // Secondary Light
val PurpleC = Color(0xFF985EFF) // Tertiary Light

val PurpleD = Color(0xFFBB86FC) // Primary Dark
val PurpleE = Color(0xFFD0BCFF) // Secondary Dark
val PurpleF = Color(0xFFE0CFFF) // Tertiary Dark


private val DarkColorScheme = darkColorScheme(
    primary = PurpleD,
    secondary = PurpleE,
    tertiary = PurpleF,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    background = Color(0xFF121212),
    surface = Color(0xFF1D1B20), // Slightly different from background for elements
    onBackground = Color(0xFFE6E1E5),
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = Color(0xFF49454F), // For card backgrounds etc. in dark
    onSurfaceVariant = Color(0xFFCAC4D0)
)

private val LightColorScheme = lightColorScheme(
    primary = PurpleA,
    secondary = PurpleB,
    tertiary = PurpleC,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.Black, // PurpleC might be light enough for black text
    background = Color(0xFFFEF7FF), // Light, slightly purplish off-white
    surface = Color(0xFFFEF7FF),
    onBackground = Color(0xFF1D1B20), // Dark grey for text on light background
    onSurface = Color(0xFF1D1B20),
    surfaceVariant = Color(0xFFE7E0EC), // For card backgrounds etc. in light
    onSurfaceVariant = Color(0xFF49454F)
)

@Composable
fun MentorMenteeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Disabling dynamic color to ensure purple theme is used
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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.setDecorFitsSystemWindows(window, false) // Edge-to-edge
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !darkTheme
            // Set navigation bar color and contrast
            // window.navigationBarColor = colorScheme.surface.toArgb() // Requires API 21
            // insetsController.isAppearanceLightNavigationBars = !darkTheme // Requires API 29
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
