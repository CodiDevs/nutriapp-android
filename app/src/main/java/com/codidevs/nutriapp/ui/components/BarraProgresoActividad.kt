package com.codidevs.nutriapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codidevs.nutriapp.ui.theme.InkSoft
import com.codidevs.nutriapp.ui.theme.Leaf
import com.codidevs.nutriapp.ui.theme.LineColor

/**
 * Barra de progreso estilizada unificada para todas las actividades.
 */
@Composable
fun BarraProgresoActividad(
    actual: Int,
    total: Int
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, LineColor.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth().height(44.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("⭐", fontSize = 18.sp)
            Spacer(Modifier.width(8.dp))
            Text(
                text = "$actual de $total",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = InkSoft
            )
            Spacer(Modifier.width(12.dp))
            LinearProgressIndicator(
                progress = { actual.toFloat() / total.toFloat() },
                modifier = Modifier
                    .weight(1f)
                    .height(10.dp)
                    .clip(CircleShape),
                color = Leaf,
                trackColor = LineColor
            )
        }
    }
}
