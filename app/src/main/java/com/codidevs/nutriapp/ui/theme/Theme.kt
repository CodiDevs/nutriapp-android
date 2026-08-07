package com.codidevs.nutriapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val NutriAppColorScheme = lightColorScheme(
    primary = Leaf,
    onPrimary = CardWhite,
    primaryContainer = LeafLight,
    secondary = Mango,
    onSecondary = CardWhite,
    secondaryContainer = MangoLight,
    tertiary = Sky,
    error = Berry,
    errorContainer = BerryLight,
    background = BgApp,
    onBackground = Ink,
    surface = Cream,
    onSurface = Ink,
    surfaceVariant = CardWhite,
    onSurfaceVariant = InkSoft,
    outline = LineColor
)

@Composable
fun NutriAppTheme(
    // Forzamos siempre el esquema claro: la identidad de la app (crema + verde)
    // no debe cambiar aunque el celular esté en modo oscuro — así evitamos
    // lo que te pasó con el "Hello Android" que salió oscuro por defecto.
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NutriAppColorScheme,
        typography = NutriAppTypography,
        content = content
    )
}