package com.codidevs.nutriapp.ui.actividades

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codidevs.nutriapp.ui.theme.*
import kotlin.random.Random

/**
 * Pantalla de premio que aparece al terminar una actividad o minijuego.
 * El mensaje, color y recompensas dependen del porcentaje de acierto.
 */
@Composable
fun PremioScreen(
    porcentaje: Int = 100,
    estrellas: Int = 0,
    monedas: Int = 0,
    puntos: Int = 0,
    medallas: Int = 0,
    onContinuar: () -> Unit
) {
    var yaPresionado by remember { mutableStateOf(false) }

    // Generar partículas de confeti solo si el resultado es excelente (>= 70%)
    val mostrarConfeti = porcentaje >= 70
    val particulas = remember {
        if (mostrarConfeti) List(50) { ParticulaConfeti.aleatoria() } else emptyList()
    }

    val (emoji, titulo, color) = when {
        porcentaje >= 70 -> Triple("🎉", "¡Excelente!", Leaf)
        porcentaje >= 40 -> Triple("👍", "¡Bien hecho!", Mango)
        else -> Triple("💪", "¡Sigue intentando!", Berry)
    }
    val mensaje = when {
        porcentaje >= 70 -> "¡Completaste la actividad!"
        porcentaje >= 40 -> "Vas muy bien, sigue practicando"
        else -> "No te rindas, ¡tú puedes!"
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (mostrarConfeti) {
            particulas.forEach { p -> LluviaConfeti(particula = p) }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = emoji, fontSize = 64.sp)
            Spacer(Modifier.height(12.dp))
            Text(text = titulo, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = color)
            Spacer(Modifier.height(8.dp))
            Text(text = mensaje, style = MaterialTheme.typography.bodyLarge, color = InkSoft, textAlign = TextAlign.Center)
            Spacer(Modifier.height(24.dp))

            // Recompensas (siempre visibles, incluso si son 0)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                PremioItem("⭐", "+$estrellas")
                Spacer(Modifier.width(10.dp))
                PremioItem("🪙", "+$monedas")
                Spacer(Modifier.width(10.dp))
                PremioItem("✨", "+$puntos")
                
                if (medallas > 0) {
                    Spacer(Modifier.width(10.dp))
                    PremioItem("🏅", "+$medallas")
                }
            }

            Spacer(Modifier.height(36.dp))

            Button(
                onClick = com.codidevs.nutriapp.data.audio.onClickConSonido {
                    if (!yaPresionado) { yaPresionado = true; onContinuar() }
                },
                enabled = !yaPresionado,
                colors = ButtonDefaults.buttonColors(containerColor = color),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text("Continuar", style = MaterialTheme.typography.labelLarge, color = Color.White)
            }
        }
    }
}

@Composable
private fun LluviaConfeti(particula: ParticulaConfeti) {
    val transition = rememberInfiniteTransition(label = "confeti")
    val yPos by transition.animateFloat(
        initialValue = -50f,
        targetValue = 1200f,
        animationSpec = infiniteRepeatable(
            animation = tween(particula.duracion, delayMillis = particula.retraso, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "y"
    )
    val rotacion by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(particula.duracion / 2, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rot"
    )

    Box(
        modifier = Modifier
            .offset(x = particula.x.dp, y = yPos.dp)
            .graphicsLayer(rotationZ = rotacion)
            .size(particula.tamano.dp)
            .background(particula.color, RoundedCornerShape(2.dp))
    )
}

private data class ParticulaConfeti(
    val x: Int,
    val color: Color,
    val duracion: Int,
    val retraso: Int,
    val tamano: Int
) {
    companion object {
        fun aleatoria(): ParticulaConfeti {
            val colores = listOf(Color.Red, Color.Yellow, Color.Blue, Color.Green, Color.Magenta, Color.Cyan)
            return ParticulaConfeti(
                x = Random.nextInt(0, 400),
                color = colores.random().copy(alpha = 0.8f),
                duracion = 2500 + Random.nextInt(1500),
                retraso = Random.nextInt(0, 3000),
                tamano = 8 + Random.nextInt(8)
            )
        }
    }
}

@Composable
private fun PremioItem(emoji: String, valor: String) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, Mango.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = emoji, fontSize = 28.sp)
            Spacer(Modifier.height(4.dp))
            Text(text = valor, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MangoDark)
        }
    }
}
