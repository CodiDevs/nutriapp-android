package com.codidevs.nutriapp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codidevs.nutriapp.ui.theme.Leaf
import com.codidevs.nutriapp.ui.theme.LeafDark
import com.codidevs.nutriapp.ui.theme.LeafLight

/**
 * Encabezado de pantalla con un botón circular de regreso (táctil)
 * y un título. Se usa en las pantallas internas (Registro, IMC, etc.).
 */
@Composable
fun ScreenHeader(
    titulo: String,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Botón circular de regreso con fondo, borde y sombra
        Box(
            modifier = Modifier
                .size(44.dp)
                .shadow(4.dp, CircleShape)
                .background(LeafLight, CircleShape)
                .border(2.dp, Leaf, CircleShape)
                .clickable(onClick = onBack),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(24.dp)) {
                val stroke = Stroke(width = 3.5.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                // Raya horizontal (sin punta de flecha)
                drawLine(
                    color = LeafDark,
                    start = Offset(size.width * 0.78f, size.height * 0.50f),
                    end = Offset(size.width * 0.22f, size.height * 0.50f),
                    strokeWidth = stroke.width
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = titulo,
            style = MaterialTheme.typography.headlineSmall,
            color = LeafDark,
            fontWeight = FontWeight.Bold
        )
    }
}
