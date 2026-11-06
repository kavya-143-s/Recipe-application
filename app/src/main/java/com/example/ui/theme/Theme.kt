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
  primary = TerracottaPrimaryDark,
  onPrimary = Color(0xFF431B05),
  primaryContainer = Color(0xFF6B2B0C),
  onPrimaryContainer = Color(0xFFFFDBCF),
  secondary = HerbSageDark,
  onSecondary = Color(0xFF13381B),
  secondaryContainer = Color(0xFF234F2B),
  onSecondaryContainer = Color(0xFFC7ECC5),
  tertiary = WarmSaffronDark,
  onTertiary = Color(0xFF3F2E00),
  background = DarkCharcoalBg,
  onBackground = DarkTextPrimary,
  surface = DarkSurface,
  onSurface = DarkTextPrimary,
  surfaceVariant = DarkSurfaceVariant,
  onSurfaceVariant = DarkTextMuted
)

private val LightColorScheme = lightColorScheme(
  primary = TerracottaPrimary,
  onPrimary = Color.White,
  primaryContainer = Color(0xFFFFDBCF),
  onPrimaryContainer = Color(0xFF3B0F00),
  secondary = HerbSage,
  onSecondary = Color.White,
  secondaryContainer = Color(0xFFC5E8C3),
  onSecondaryContainer = Color(0xFF07210C),
  tertiary = WarmSaffron,
  onTertiary = Color.White,
  background = WarmCreamBg,
  onBackground = TextCharcoal,
  surface = WarmSurface,
  onSurface = TextCharcoal,
  surfaceVariant = WarmSurfaceVariant,
  onSurfaceVariant = TextMuted
)

@Composable
fun FlavorCraftTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Set false by default to showcase our bespoke culinary branding!
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

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) = FlavorCraftTheme(darkTheme, dynamicColor, content)
