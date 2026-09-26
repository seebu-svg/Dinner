package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = AmberGoldLight,
  onPrimary = DarkEspressoBg,
  primaryContainer = DarkSurfaceHighlight,
  onPrimaryContainer = AmberGoldLight,
  secondary = OliveSageLight,
  onSecondary = DarkEspressoBg,
  secondaryContainer = DarkSurfaceElevated,
  onSecondaryContainer = OliveSageLight,
  tertiary = TerracottaAccent,
  onTertiary = Color.White,
  background = DarkEspressoBg,
  onBackground = DarkTextPrimary,
  surface = DarkSurface,
  onSurface = DarkTextPrimary,
  surfaceVariant = DarkSurfaceElevated,
  onSurfaceVariant = DarkTextSecondary,
  outline = DarkTextMuted,
)

private val LightColorScheme = lightColorScheme(
  primary = AmberGoldDark,
  onPrimary = Color.White,
  primaryContainer = LightSurfaceElevated,
  onPrimaryContainer = AmberGoldDark,
  secondary = OliveSage,
  onSecondary = Color.White,
  secondaryContainer = LightSurfaceElevated,
  onSecondaryContainer = OliveSage,
  tertiary = TerracottaAccent,
  onTertiary = Color.White,
  background = LightCreamBg,
  onBackground = LightTextPrimary,
  surface = LightSurface,
  onSurface = LightTextPrimary,
  surfaceVariant = LightSurfaceElevated,
  onSurfaceVariant = LightTextSecondary,
  outline = LightTextSecondary,
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Use our signature warm hospitality dining theme
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
