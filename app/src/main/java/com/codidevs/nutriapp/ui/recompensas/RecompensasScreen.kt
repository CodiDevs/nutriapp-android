package com.codidevs.nutriapp.ui.recompensas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codidevs.nutriapp.data.models.MedallaInfo
import com.codidevs.nutriapp.ui.theme.Ink
import com.codidevs.nutriapp.ui.theme.InkSoft
import com.codidevs.nutriapp.ui.theme.LeafDark
import com.codidevs.nutriapp.ui.theme.LeafLight
import com.codidevs.nutriapp.ui.theme.LineColor
import com.codidevs.nutriapp.ui.theme.Mango
import com.codidevs.nutriapp.ui.theme.MangoDark

/**
 * Pantalla de Recompensas: aparece como un panel que sube desde abajo (~70% de la
 * pantalla) mostrando las medallas/recompensas canjeadas y las que faltan por canjear
 * con monedas. La medalla especial se destaca con estilo propio.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecompensasScreen(
    monedas: Int,
    medallas: List<MedallaInfo>,
    canjeadas: Set<String>,
    onCanjear: (MedallaInfo) -> Unit,
    onCerrar: () -> Unit
) {
    // Panel que sube desde abajo
    ModalBottomSheet(
        onDismissRequest = onCerrar,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 560.dp)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 8.dp)
        ) {
            // Manija
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(width = 44.dp, height = 5.dp)
                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.4f), RoundedCornerShape(50))
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "🏆 Recompensas",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Ink
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "Canjea tus monedas por medallas",
                style = MaterialTheme.typography.bodyMedium,
                color = InkSoft,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            // Monedas disponibles
            Surface(
                color = Mango.copy(alpha = 0.15f),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "🪙 $monedas monedas disponibles",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MangoDark,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            // Medallas
            medallas.forEach { medalla ->
                RecompensaItem(
                    medalla = medalla,
                    monedas = monedas,
                    canjeada = medalla.id in canjeadas,
                    onCanjear = { onCanjear(medalla) }
                )
                Spacer(Modifier.height(10.dp))
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
private fun RecompensaItem(
    medalla: MedallaInfo,
    monedas: Int,
    canjeada: Boolean,
    onCanjear: () -> Unit
) {
    val costo = if (medalla.especial) 500 else 200
    val puede = monedas >= costo && medalla.desbloqueada

    Surface(
        color = when {
            canjeada -> LeafLight
            medalla.especial -> Color(0xFFFFF3E0) // tono especial
            else -> Color.White
        },
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
            when {
                canjeada -> 3.dp
                medalla.especial -> 2.dp
                else -> 2.dp
            },
            when {
                canjeada -> LeafDark
                medalla.especial -> Mango
                else -> LineColor
            }
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = medalla.emoji,
                fontSize = 30.sp
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = medalla.nombre,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Ink
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = if (medalla.especial)
                        "⭐ Medalla especial · ${medalla.descripcion}"
                    else
                        medalla.descripcion,
                    style = MaterialTheme.typography.labelSmall,
                    color = InkSoft
                )
            }
            Spacer(Modifier.width(8.dp))
            when {
                canjeada -> Text("✓", fontSize = 22.sp, color = LeafDark, fontWeight = FontWeight.Bold)
                !medalla.desbloqueada -> Text("🔒", fontSize = 18.sp)
                else -> {
                    // Botón de canje
                    Surface(
                        color = if (puede) Mango else LineColor.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.clickable(enabled = puede, onClick = onCanjear)
                    ) {
                        Text(
                            text = "🪙 $costo",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (puede) Color.White else InkSoft,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
