package com.codidevs.nutriapp.ui.perfil

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codidevs.nutriapp.ui.theme.Ink
import com.codidevs.nutriapp.ui.theme.InkSoft
import com.codidevs.nutriapp.ui.theme.LeafDark
import com.codidevs.nutriapp.ui.theme.LineColor
import com.codidevs.nutriapp.ui.theme.Mango
import com.codidevs.nutriapp.ui.theme.MangoDark
import com.codidevs.nutriapp.ui.theme.Sky

private data class StatPerfil(
    val valor: String,
    val etiqueta: String
)

private val STATS = listOf(
    StatPerfil("240", "Monedas"),
    StatPerfil("6", "Actividades"),
    StatPerfil("18", "Correctas"),
    StatPerfil("32 min", "Aprendido"),
    StatPerfil("5 días", "Racha"),
    StatPerfil("1", "Medallas")
)

private data class Medalla(
    val emoji: String,
    val nombre: String,
    val bloqueada: Boolean = false
)

private val MEDALLAS = listOf(
    Medalla("🥇", "Explorador de frutas"),
    Medalla("🥇", "Rey de verduras", bloqueada = true),
    Medalla("🥇", "Campeón del agua", bloqueada = true)
)

@Composable
fun PerfilScreen(
    nombre: String,
    onVerRecompensas: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Spacer(Modifier.height(4.dp))

        // Cabecera del perfil
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = Mango,
                shape = CircleShape,
                border = BorderStroke(3.dp, MangoDark),
                modifier = Modifier.size(66.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "🦸", fontSize = 34.sp)
                }
            }
            Spacer(Modifier.width(14.dp))
            Column {
                Text(
                    text = nombre,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Ink
                )
                Text(
                    text = "Nivel 1 · Explorador de alimentos",
                    style = MaterialTheme.typography.bodyMedium,
                    color = InkSoft,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Estadísticas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatGridCell(STATS[0].valor, STATS[0].etiqueta, Modifier.weight(1f))
            StatGridCell(STATS[1].valor, STATS[1].etiqueta, Modifier.weight(1f))
            StatGridCell(STATS[2].valor, STATS[2].etiqueta, Modifier.weight(1f))
        }
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatGridCell(STATS[3].valor, STATS[3].etiqueta, Modifier.weight(1f))
            StatGridCell(STATS[4].valor, STATS[4].etiqueta, Modifier.weight(1f))
            StatGridCell(STATS[5].valor, STATS[5].etiqueta, Modifier.weight(1f))
        }

        Spacer(Modifier.height(16.dp))

        // Mis medallas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mis medallas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Ink
            )
            Text(
                text = "Ver todas →",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Sky
            )
        }

        Spacer(Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MEDALLAS.forEach { medalla ->
                MedalCell(medalla, Modifier.weight(1f))
            }
        }

        Spacer(Modifier.height(16.dp))

        // Ver recompensas
        Button(
            onClick = onVerRecompensas,
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(2.dp, LineColor),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text(
                text = "Ver recompensas",
                style = MaterialTheme.typography.labelLarge,
                color = LeafDark
            )
        }

        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun StatGridCell(valor: String, etiqueta: String, modifier: Modifier = Modifier) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(2.dp, LineColor),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = valor,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = LeafDark
            )
            Text(
                text = etiqueta,
                style = MaterialTheme.typography.labelSmall,
                color = InkSoft,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun MedalCell(medalla: Medalla, modifier: Modifier = Modifier) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(2.dp, LineColor),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = medalla.emoji,
                fontSize = 26.sp,
                modifier = Modifier.alpha(if (medalla.bloqueada) 0.45f else 1f)
            )
            Text(
                text = medalla.nombre,
                style = MaterialTheme.typography.labelSmall,
                color = if (medalla.bloqueada) InkSoft.copy(alpha = 0.5f) else Ink,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}
