package com.codidevs.nutriapp.ui.juegos

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import com.codidevs.nutriapp.ui.theme.LeafDark
import com.codidevs.nutriapp.ui.theme.LeafLight
import com.codidevs.nutriapp.ui.theme.LineColor

data class Minijuego(
    val id: String,
    val emoji: String,
    val nombre: String
)

private val MINIJUEGOS = listOf(
    Minijuego("arrastrar", "🥦", "Arrastrar"),
    Minijuego("vf", "✅", "V o F"),
    Minijuego("completa", "✏️", "Completa"),
    Minijuego("mejor", "🍽️", "Mejor opción"),
    Minijuego("ruleta", "🎡", "Ruleta"),
    Minijuego("memoria", "🧠", "Memoria")
)

@Composable
fun JuegosScreen(
    completados: Set<String>,
    onMinijuegoClick: (String) -> Unit
) {
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

        Spacer(Modifier.height(4.dp))

        Text(
            text = "¡Practica sin límites y gana monedas!",
            style = MaterialTheme.typography.bodyMedium,
            color = InkSoft,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        // Grilla 2x2 de minijuegos con tarjetas grandes
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            MINIJUEGOS.chunked(2).forEach { fila ->
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    fila.forEach { juego ->
                        MinijuegoTarjeta(
                            juego = juego,
                            completado = juego.id in completados,
                            onClick = { onMinijuegoClick(juego.id) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (fila.size == 1) Spacer(Modifier.weight(1f))
                }
            }
        }

        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun MinijuegoTarjeta(
    juego: Minijuego,
    completado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = if (completado) LeafLight else Color.White,
        shape = RoundedCornerShape(22.dp),
        border = BorderStroke(3.dp, if (completado) Leaf else LineColor),
        shadowElevation = 3.dp,
        modifier = modifier
            .height(130.dp)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Círculo con el emoji
            Surface(
                color = if (completado) Leaf else LeafLight,
                shape = RoundedCornerShape(50),
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = juego.emoji, fontSize = 28.sp)
                }
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = juego.nombre,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (completado) LeafDark else LeafDark,
                textAlign = TextAlign.Center
            )
            if (completado) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "✓ Completado",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = LeafDark
                )
            }
        }
    }
}
