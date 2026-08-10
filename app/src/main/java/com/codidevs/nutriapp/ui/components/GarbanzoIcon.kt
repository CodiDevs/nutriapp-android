package com.codidevs.nutriapp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

/**
 * Dibuja un garbanzo con Canvas: forma ovalada crema con su puntita característica,
 * sombra y brillo. Estilo flat pero reconocible.
 */
@Composable
fun GarbanzoIcon(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f

        // Cuerpo del garbanzo: óvalo inclinado
        val cuerpo = Path().apply {
            // Óvalo ligeramente rotado (forma de garbanzo)
            addOval(
                androidx.compose.ui.geometry.Rect(
                    left = cx - w * 0.30f,
                    top = cy - h * 0.36f,
                    right = cx + w * 0.34f,
                    bottom = cy + h * 0.36f
                )
            )
        }

        // Base del garbanzo (color crema-amarillo)
        drawPath(
            path = cuerpo,
            color = Color(0xFFF5D9A0)
        )

        // La puntita del garbanzo (parte superior, más puntiaguda)
        val punta = Path().apply {
            moveTo(cx + w * 0.08f, cy - h * 0.38f)
            // Curva hacia arriba para la puntita
            cubicTo(
                cx + w * 0.02f, cy - h * 0.52f,
                cx - w * 0.18f, cy - h * 0.30f,
                cx - w * 0.14f, cy - h * 0.10f
            )
            // Vuelve al cuerpo
            cubicTo(
                cx - w * 0.10f, cy - h * 0.05f,
                cx + w * 0.02f, cy - h * 0.22f,
                cx + w * 0.08f, cy - h * 0.38f
            )
            close()
        }
        drawPath(
            path = punta,
            color = Color(0xFFF5D9A0)
        )

        // Contorno del garbanzo
        drawPath(
            path = cuerpo,
            color = Color(0xFFD9B96A),
            style = Stroke(width = (w * 0.05f).coerceAtLeast(2f))
        )
        drawPath(
            path = punta,
            color = Color(0xFFD9B96A),
            style = Stroke(width = (w * 0.05f).coerceAtLeast(2f))
        )

        // Brillo (parte superior izquierda)
        drawOval(
            color = Color(0x66FFFFFF),
            topLeft = Offset(cx - w * 0.18f, cy - h * 0.24f),
            size = Size(w * 0.16f, h * 0.10f)
        )

        // Detalle de la hendidura (línea curva del garbanzo)
        val hendidura = Path().apply {
            moveTo(cx - w * 0.22f, cy + h * 0.05f)
            cubicTo(
                cx - w * 0.05f, cy + h * 0.22f,
                cx + w * 0.10f, cy + h * 0.10f,
                cx + w * 0.20f, cy - h * 0.10f
            )
        }
        drawPath(
            path = hendidura,
            color = Color(0xFFC9A95E),
            style = Stroke(width = (w * 0.035f).coerceAtLeast(1.5f), cap = androidx.compose.ui.graphics.StrokeCap.Round)
        )
    }
}
