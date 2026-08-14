package com.codidevs.nutriapp.ui.components

import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale

/**
 * Aplica una animación de pulso (latido) al modifier.
 * Se usa para guiar al niño hacia los botones de acción.
 */
@Composable
fun Modifier.pulsoAnimado(enabled: Boolean = true, escalaFinal: Float = 1.06f): Modifier {
    if (!enabled) return this
    
    val infiniteTransition = rememberInfiniteTransition(label = "pulso")
    val escala by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = escalaFinal,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing), // Más lento y suave
            repeatMode = RepeatMode.Reverse
        ),
        label = "escala"
    )
    
    return this.scale(escala)
}
