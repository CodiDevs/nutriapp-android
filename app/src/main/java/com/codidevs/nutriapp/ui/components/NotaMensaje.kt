package com.codidevs.nutriapp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Nota adhesiva con la esquina superior izquierda doblada.
 * Se usa para mensajes motivadores (parece una nota, no un botón).
 */
@Composable
fun NotaMensaje(
    emoji: String,
    titulo: String,
    subtitulo: String? = null,
    colorFondo: Color,
    colorTexto: Color,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        // Sombra de la nota (desplazada)
        Surface(
            color = Color(0x22000000),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = 4.dp, y = 4.dp)
        ) {}
        Surface(
            color = colorFondo,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = emoji, fontSize = 36.sp)
                Spacer(Modifier.height(4.dp))
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = colorTexto,
                    textAlign = TextAlign.Center
                )
                if (subtitulo != null) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = subtitulo,
                        style = MaterialTheme.typography.bodySmall,
                        color = colorTexto.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        // Esquina doblada (triángulo en la esquina superior izquierda)
        Canvas(
            modifier = Modifier
                .size(22.dp)
                .align(Alignment.TopStart)
        ) {
            val plegado = Path().apply {
                moveTo(0f, 0f)
                lineTo(size.width, 0f)
                lineTo(0f, size.height)
                close()
            }
            // Sombra del doblez
            drawPath(plegado, Color(0x33000000))
            val doblez = Path().apply {
                moveTo(size.width * 0.15f, 0f)
                lineTo(size.width, size.height * 0.15f)
                lineTo(size.width, size.height * 0.35f)
                lineTo(size.width * 0.35f, size.height)
                lineTo(0f, size.height * 0.85f)
                lineTo(size.width * 0.15f, 0f)
                close()
            }
            drawPath(doblez, Color(0xFFFFF8E1))
        }
    }
}
