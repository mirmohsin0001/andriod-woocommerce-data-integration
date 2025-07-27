package com.sparrow.amp2.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = ProductShowcasePrimary,
    onPrimary = ProductShowcaseOnPrimary,
    primaryContainer = ProductShowcasePrimaryContainer,
    onPrimaryContainer = ProductShowcaseOnPrimaryContainer,
    secondary = ProductShowcaseSecondary,
    onSecondary = ProductShowcaseOnSecondary,
    secondaryContainer = ProductShowcaseSecondaryContainer,
    onSecondaryContainer = ProductShowcaseOnSecondaryContainer,
    tertiary = ProductShowcaseTertiary,
    onTertiary = ProductShowcaseOnTertiary,
    tertiaryContainer = ProductShowcaseTertiaryContainer,
    onTertiaryContainer = ProductShowcaseOnTertiaryContainer,
    error = ProductShowcaseError,
    onError = ProductShowcaseOnError,
    errorContainer = ProductShowcaseErrorContainer,
    onErrorContainer = ProductShowcaseOnErrorContainer,
    background = ProductShowcaseDarkBackground,
    onBackground = ProductShowcaseOnDarkBackground,
    surface = ProductShowcaseDarkSurface,
    onSurface = ProductShowcaseOnDarkSurface,
    surfaceVariant = ProductShowcaseDarkSurfaceVariant,
    onSurfaceVariant = ProductShowcaseOnDarkSurfaceVariant,
    outline = ProductShowcaseDarkOutline,
    outlineVariant = ProductShowcaseDarkOutlineVariant,
    scrim = ProductShowcaseScrim,
    inverseSurface = ProductShowcaseLightSurface,
    inverseOnSurface = ProductShowcaseOnLightSurface,
    inversePrimary = ProductShowcasePrimary
)

private val LightColorScheme = lightColorScheme(
    primary = ProductShowcasePrimary,
    onPrimary = ProductShowcaseOnPrimary,
    primaryContainer = ProductShowcasePrimaryContainer,
    onPrimaryContainer = ProductShowcaseOnPrimaryContainer,
    secondary = ProductShowcaseSecondary,
    onSecondary = ProductShowcaseOnSecondary,
    secondaryContainer = ProductShowcaseSecondaryContainer,
    onSecondaryContainer = ProductShowcaseOnSecondaryContainer,
    tertiary = ProductShowcaseTertiary,
    onTertiary = ProductShowcaseOnTertiary,
    tertiaryContainer = ProductShowcaseTertiaryContainer,
    onTertiaryContainer = ProductShowcaseOnTertiaryContainer,
    error = ProductShowcaseError,
    onError = ProductShowcaseOnError,
    errorContainer = ProductShowcaseErrorContainer,
    onErrorContainer = ProductShowcaseOnErrorContainer,
    background = ProductShowcaseLightBackground,
    onBackground = ProductShowcaseOnLightBackground,
    surface = ProductShowcaseLightSurface,
    onSurface = ProductShowcaseOnLightSurface,
    surfaceVariant = ProductShowcaseLightSurfaceVariant,
    onSurfaceVariant = ProductShowcaseOnLightSurfaceVariant,
    outline = ProductShowcaseLightOutline,
    outlineVariant = ProductShowcaseLightOutlineVariant,
    scrim = ProductShowcaseScrim,
    inverseSurface = ProductShowcaseDarkSurface,
    inverseOnSurface = ProductShowcaseOnDarkSurface,
    inversePrimary = ProductShowcasePrimary
)

@Composable
fun ProductShowcaseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ProductShowcaseTypography,
        content = content
    )
}