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
import com.codidevs.nutriapp.ui.theme.Ink
import com.codidevs.nutriapp.ui.theme.InkSoft
import com.codidevs.nutriapp.ui.theme.Leaf
import com.codidevs.nutriapp.ui.theme.Mango
import com.codidevs.nutriapp.ui.theme.MangoDark

/**
 * Pantalla de premio que aparece al completar una actividad:
 * "¡Excelente! Ganaste por completar la actividad" + recompensas (⭐🪙🏅).
 */
@Composable
fun PremioScreen(
    estrellas: Int = 3,
    monedas: Int = 20,
    medallas: Int = 1,
    onContinuar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "🎉", fontSize = 64.sp)

        Spacer(Modifier.height(12.dp))

        Text(
            text = "¡Excelente!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Leaf
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Ganaste por completar la actividad",
            style = MaterialTheme.typography.bodyLarge,
            color = InkSoft,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(24.dp))

        // Recompensas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            PremioItem("⭐", "+$estrellas")
            PremioItem("🪙", "+$monedas")
            PremioItem("🏅", "+$medallas")
        }

        Spacer(Modifier.height(36.dp))

        Button(
            onClick = onContinuar,
            colors = ButtonDefaults.buttonColors(containerColor = Mango),
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
