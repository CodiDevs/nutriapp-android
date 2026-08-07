package com.codidevs.nutriapp.ui.juegos

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codidevs.nutriapp.ui.theme.Ink
import com.codidevs.nutriapp.ui.theme.InkSoft
import com.codidevs.nutriapp.ui.theme.LineColor

private data class Minijuego(
    val emoji: String,
    val nombre: String
)

private val MINIJUEGOS = listOf(
    Minijuego("🥦", "Arrastrar"),
    Minijuego("✅", "V o F"),
    Minijuego("✏️", "Completa"),
    Minijuego("🍽️", "Mejor opción"),
    Minijuego("🎡", "Ruleta"),
    Minijuego("🧠", "Memoria")
)

@Composable
fun JuegosScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Spacer(Modifier.height(4.dp))

        Text(
            text = "Minijuegos",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Ink
        )

        Spacer(Modifier.height(16.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            MINIJUEGOS.forEach { juego ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(2.dp, LineColor),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = juego.emoji, fontSize = 30.sp)
                        Spacer(Modifier.width(14.dp))
                        Text(
                            text = juego.nombre,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Ink
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))
    }
}
