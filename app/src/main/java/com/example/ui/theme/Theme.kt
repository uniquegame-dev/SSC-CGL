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

private val DarkColorScheme =
  darkColorScheme(
    primary = PrimaryBlueLight,
    onPrimary = Color.White,
    primaryContainer = PrimaryBlueDark,
    onPrimaryContainer = Color.White,
    secondary = AmberSecondary,
    onSecondary = Color.White,
    background = LightBackground,
    onBackground = TextDarkHeading,
    surface = LightSurface,
    onSurface = TextDarkHeading,
    surfaceVariant = LightCardBackground,
    onSurfaceVariant = TextMediumGray,
    outline = BorderLight,
  )

private val LightColorScheme =
  lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDBEAFE),
    onPrimaryContainer = TextDarkHeading,
    secondary = AmberSecondary,
    onSecondary = Color.White,
    secondaryContainer = AmberContainer,
    onSecondaryContainer = Color(0xFF78350F),
    background = LightBackground,
    onBackground = TextDarkHeading,
    surface = LightSurface,
    onSurface = TextDarkHeading,
    surfaceVariant = LightCardBackground,
    onSurfaceVariant = TextMediumGray,
    outline = BorderLight,
    outlineVariant = BorderSubtle,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = false,
  // Dynamic color is available on Android 12+
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}


