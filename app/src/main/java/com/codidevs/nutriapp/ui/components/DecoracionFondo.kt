package com.codidevs.nutriapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

/**
 * Componente enriquecido para añadir un fondo decorativo con emojis transparentes.
 */
@Composable
fun DecoracionFondoActividad() {
    val items = remember {
        val emojis = listOf("🍎", "🥦", "🍓", "🍃", "✨", "⭐", "💧", "🥕", "🍌", "🥑", "🌽", "🍇", "🍊", "🫧", "🍀", "🌸")
        List(25) { // Aumentamos a 25 elementos para que se vea más lleno
            Triple(
                emojis.random(),
                Random.nextInt(0, 360).dp, // Posición X
                Random.nextInt(0, 800).dp  // Posición Y
            )
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        items.forEach { (emoji, x, y) ->
            Text(
                text = emoji,
                fontSize = (20 + Random.nextInt(10)).sp,
                modifier = Modifier
                    .offset(x = x, y = y)
                    .alpha(0.15f) // Un poco más visible (sube de 0.08 a 0.15)
                    .rotate(Random.nextInt(360).toFloat())
            )
        }
    }
}
