package com.codidevs.nutriapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codidevs.nutriapp.ui.theme.Ink
import com.codidevs.nutriapp.ui.theme.InkSoft
import com.codidevs.nutriapp.ui.theme.LineColor

/**
 * Barra de estadísticas (racha de días activos, estrellas y monedas) que aparece
 * en las pantallas principales (Home, Sendero, Juegos, Perfil).
 */
@Composable
fun StatsBar(
    racha: String = "🔥 0",
    estrellas: String = "⭐ 0",
    monedas: String = "🪙 0",
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        StatsChip(racha, Modifier.weight(1f))
        StatsChip(estrellas, Modifier.weight(1f))
        StatsChip(monedas, Modifier.weight(1f))
    }
}

@Composable
private fun StatsChip(text: String, modifier: Modifier = Modifier) {
    Surface(
        color = androidx.compose.ui.graphics.Color.White,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(2.dp, LineColor),
        modifier = modifier.height(48.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = androidx.compose.material3.MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Ink,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
