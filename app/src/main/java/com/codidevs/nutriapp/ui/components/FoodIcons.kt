package com.codidevs.nutriapp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke

/** Icono de papaya dibujado: fruta ovalada alargada naranja-verde con semillas. */
@Composable
fun PapayaIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f

        // Cuerpo de la papaya (óvalo alargado)
        val cuerpo = Path().apply {
            addOval(Rect(cx - w * 0.22f, cy - h * 0.42f, cx + w * 0.22f, cy + h * 0.42f))
        }
        drawPath(cuerpo, Color(0xFFF5A742)) // naranja papaya

        // Mitad verde (parte superior, cáscara)
        drawArc(
            color = Color(0xFF6BBF59),
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(cx - w * 0.22f, cy - h * 0.42f),
            size = Size(w * 0.44f, h * 0.84f)
        )

        // Línea central
        drawLine(
            color = Color(0xFFE08A2E),
            start = Offset(cx, cy - h * 0.40f),
            end = Offset(cx, cy + h * 0.40f),
            strokeWidth = w * 0.03f
        )

        // Semillas negras en el centro
        val semillas = listOf(
            Offset(cx - w * 0.10f, cy - h * 0.15f),
            Offset(cx + w * 0.10f, cy - h * 0.15f),
            Offset(cx, cy),
            Offset(cx - w * 0.08f, cy + h * 0.14f),
            Offset(cx + w * 0.08f, cy + h * 0.14f)
        )
        semillas.forEach { s ->
            drawCircle(Color(0xFF3A3A3A), radius = w * 0.045f, center = s)
        }

        // Contorno
        drawPath(cuerpo, Color(0xFFD97E1E), style = Stroke(w * 0.04f))
    }
}

/** Icono de lentejas dibujado: platito con lentejas marrones. */
@Composable
fun LentejasIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f

        // Plato (elipse marrón claro)
        drawOval(
            color = Color(0xFFE8D5B5),
            topLeft = Offset(cx - w * 0.40f, cy - h * 0.05f),
            size = Size(w * 0.80f, h * 0.55f)
        )
        // Plato interior
        drawOval(
            color = Color(0xFFF5E8D0),
            topLeft = Offset(cx - w * 0.32f, cy - h * 0.01f),
            size = Size(w * 0.64f, h * 0.42f)
        )
        // Lentejas (círculos marrones)
        val lentejas = listOf(
            Offset(cx - w * 0.18f, cy - h * 0.02f),
            Offset(cx, cy - h * 0.06f),
            Offset(cx + w * 0.18f, cy - h * 0.02f),
            Offset(cx - w * 0.10f, cy + h * 0.10f),
            Offset(cx + w * 0.10f, cy + h * 0.10f),
            Offset(cx, cy + h * 0.02f)
        )
        lentejas.forEach { l ->
            drawCircle(Color(0xFFB5813D), radius = w * 0.07f, center = l)
            drawCircle(Color(0xFFD4A055), radius = w * 0.035f, center = Offset(l.x - w * 0.02f, l.y - w * 0.02f))
        }
    }
}

/** Icono de espinaca dibujado: hoja verde con nervaduras. */
@Composable
fun EspinacaIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f

        // Hoja (forma de hoja ancha con pecíolo)
        val hoja = Path().apply {
            moveTo(cx - w * 0.05f, cy + h * 0.40f) // base
            cubicTo(
                cx - w * 0.35f, cy + h * 0.10f,
                cx - w * 0.30f, cy - h * 0.30f,
                cx, cy - h * 0.40f
            )
            cubicTo(
                cx + w * 0.30f, cy - h * 0.30f,
                cx + w * 0.35f, cy + h * 0.10f,
                cx + w * 0.05f, cy + h * 0.40f
            )
            close()
        }
        drawPath(hoja, Color(0xFF4CAF50))

        // Nervadura central
        drawLine(
            color = Color(0xFF2E7D32),
            start = Offset(cx, cy + h * 0.38f),
            end = Offset(cx, cy - h * 0.36f),
            strokeWidth = w * 0.04f
        )
        // Nervaduras laterales
        listOf(-0.20f, -0.05f, 0.10f).forEach { yOff ->
            drawLine(
                color = Color(0xFF2E7D32),
                start = Offset(cx, cy + h * yOff),
                end = Offset(cx - w * 0.24f, cy + h * (yOff + 0.10f)),
                strokeWidth = w * 0.025f
            )
            drawLine(
                color = Color(0xFF2E7D32),
                start = Offset(cx, cy + h * yOff),
                end = Offset(cx + w * 0.24f, cy + h * (yOff + 0.10f)),
                strokeWidth = w * 0.025f
            )
        }
    }
}

/** Icono de tomate dibujado: 3 tomates rojos con hojita (se distingue de la manzana). */
@Composable
fun TomatesIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Tres tomates: uno grande al frente, dos atrás
        val tomates = listOf(
            Triple(Offset(w * 0.30f, h * 0.38f), w * 0.26f, 0f),   // atrás izquierda
            Triple(Offset(w * 0.72f, h * 0.34f), w * 0.24f, 0f),   // atrás derecha
            Triple(Offset(w * 0.50f, h * 0.62f), w * 0.34f, 0f)    // frente grande
        )
        tomates.forEach { (centro, radio, _) ->
            // Cuerpo rojo
            drawCircle(Color(0xFFE53935), radius = radio, center = centro)
            // Brillo
            drawCircle(
                Color(0x66FF8A80),
                radius = radio * 0.30f,
                center = Offset(centro.x - radio * 0.30f, centro.y - radio * 0.30f)
            )
            // Hojita verde arriba
            val hoja = Path().apply {
                moveTo(centro.x, centro.y - radio * 0.95f)
                cubicTo(
                    centro.x - radio * 0.35f, centro.y - radio * 1.35f,
                    centro.x - radio * 0.60f, centro.y - radio * 0.90f,
                    centro.x - radio * 0.30f, centro.y - radio * 0.70f
                )
                close()
            }
            drawPath(hoja, Color(0xFF43A047))
            val hoja2 = Path().apply {
                moveTo(centro.x, centro.y - radio * 0.95f)
                cubicTo(
                    centro.x + radio * 0.35f, centro.y - radio * 1.35f,
                    centro.x + radio * 0.60f, centro.y - radio * 0.90f,
                    centro.x + radio * 0.30f, centro.y - radio * 0.70f
                )
                close()
            }
            drawPath(hoja2, Color(0xFF43A047))
        }
    }
}

/** Icono de yogur dibujado: vaso con tapa. */
@Composable
fun YogurIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f

        // Vaso (trapecio)
        val vaso = Path().apply {
            moveTo(cx - w * 0.30f, h * 0.25f)
            lineTo(cx + w * 0.30f, h * 0.25f)
            lineTo(cx + w * 0.22f, h * 0.85f)
            lineTo(cx - w * 0.22f, h * 0.85f)
            close()
        }
        drawPath(vaso, Color(0xFFFFFFFF))

        // Yogur dentro
        drawRect(
            color = Color(0xFFFFF6E5),
            topLeft = Offset(cx - w * 0.26f, h * 0.30f),
            size = Size(w * 0.52f, h * 0.45f)
        )
        // Cuchara de yogur (arriba)
        drawOval(
            color = Color(0xFFB3D4F0),
            topLeft = Offset(cx - w * 0.12f, h * 0.14f),
            size = Size(w * 0.24f, h * 0.12f)
        )

        // Contorno del vaso
        drawPath(vaso, Color(0xFFE0D8C8), style = Stroke(w * 0.04f))
    }
}
