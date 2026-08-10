package com.codidevs.nutriapp.ui.actividades

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codidevs.nutriapp.ui.theme.Berry
import com.codidevs.nutriapp.ui.theme.Ink
import com.codidevs.nutriapp.ui.theme.InkSoft
import com.codidevs.nutriapp.ui.theme.Leaf
import com.codidevs.nutriapp.ui.theme.LeafDark
import com.codidevs.nutriapp.ui.theme.Mango
import com.codidevs.nutriapp.ui.theme.MangoDark

/**
 * Pantalla de premio que aparece al terminar una actividad o minijuego.
 * El mensaje, color y recompensas dependen del porcentaje de acierto:
 * - >=70% (2-3 estrellas): "¡Excelente!" verde, recompensas completas.
 * - >=40% (1 estrella): "¡Bien hecho!" naranja, recompensas a la mitad.
 * - <40% (0 estrellas): "¡Sigue intentando!" rojo, sin recompensas.
 */
@Composable
fun PremioScreen(
    porcentaje: Int = 100,
    estrellas: Int = 0,
    monedas: Int = 0,
    medallas: Int = 0,
    onContinuar: () -> Unit
) {
    // Evita que el niño presione Continuar muchas veces y rompa la navegación
    var yaPresionado by remember { mutableStateOf(false) }

    // Mensaje y color según desempeño (porcentaje); las estrellas mostradas son la recompensa asignada.
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = emoji, fontSize = 64.sp)

        Spacer(Modifier.height(12.dp))

        Text(
            text = titulo,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = mensaje,
            style = MaterialTheme.typography.bodyLarge,
            color = InkSoft,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(24.dp))

        // Recompensas (solo si ganó algo)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            if (estrellas > 0) PremioItem("⭐", "+$estrellas")
            if (monedas > 0) PremioItem("🪙", "+$monedas")
            if (medallas > 0) PremioItem("🏅", "+$medallas")
        }

        Spacer(Modifier.height(36.dp))

        Button(
            onClick = {
                if (!yaPresionado) {
                    yaPresionado = true
                    onContinuar()
                }
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
            Text(
                text = valor,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MangoDark
            )
        }
    }
}
