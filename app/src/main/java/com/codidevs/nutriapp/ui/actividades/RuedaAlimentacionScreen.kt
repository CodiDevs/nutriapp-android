package com.codidevs.nutriapp.ui.actividades

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codidevs.nutriapp.ui.components.ScreenHeader
import com.codidevs.nutriapp.ui.theme.Berry
import com.codidevs.nutriapp.ui.theme.BerryLight
import com.codidevs.nutriapp.ui.theme.Ink
import com.codidevs.nutriapp.ui.theme.InkSoft
import com.codidevs.nutriapp.ui.theme.Leaf
import com.codidevs.nutriapp.ui.theme.LeafDark
import com.codidevs.nutriapp.ui.theme.LeafLight
import com.codidevs.nutriapp.ui.theme.LineColor
import com.codidevs.nutriapp.ui.theme.Mango
import com.codidevs.nutriapp.ui.theme.MangoDark
import com.codidevs.nutriapp.ui.theme.Sky
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.random.Random

private data class AlimentoRuleta(
    val emoji: String,
    val nombre: String,
    val aporte: String,
    val opciones: List<String>
)

/** Alimentos de la rueda con su pregunta "¿Qué aporta?", según las indicaciones. */
private val ALIMENTOS_RULETA = listOf(
    AlimentoRuleta("🥚", "Huevo", "Proteínas", listOf("Proteínas", "Mucha azúcar", "Solo agua", "Nada")),
    AlimentoRuleta("🍎", "Manzana", "Vitaminas", listOf("Vitaminas", "Mucha azúcar", "Solo agua", "Nada")),
    AlimentoRuleta("🥛", "Leche", "Calcio", listOf("Calcio", "Grasas", "Solo agua", "Nada")),
    AlimentoRuleta("🍞", "Pan", "Energía", listOf("Energía", "Proteínas", "Solo agua", "Nada")),
    AlimentoRuleta("🥦", "Brócoli", "Fibra", listOf("Fibra", "Mucha azúcar", "Solo agua", "Nada")),
    AlimentoRuleta("🐟", "Pescado", "Omega-3", listOf("Omega-3", "Mucha azúcar", "Solo agua", "Nada"))
)

private val COLORES_RULETA = listOf(
    Leaf, Mango, Sky, Berry, Color(0xFFB98CE0), Color(0xFFD9C86A)
)

/**
 * Actividad "Rueda de la alimentación": la ruleta gira (siempre hacia adelante,
 * con frenado suave) y cae en un alimento; luego el niño elige qué aporta.
 */
@Composable
fun RuedaAlimentacionScreen(
    onBack: () -> Unit,
    onTerminada: (puntaje: Int) -> Unit
) {
    var indice by remember { mutableStateOf(0) }
    var puntaje by remember { mutableStateOf(0) }
    var seleccionada by remember { mutableStateOf<String?>(null) }
    var girando by remember { mutableStateOf(false) }
    val rotacion = remember { Animatable(0f) }
    val escala = remember { Animatable(1f) }
    // Partículas decorativas que se mueven mientras gira la ruleta
    var particulas by remember { mutableStateOf<List<ParticulaRuleta>>(emptyList()) }

    val alimento = ALIMENTOS_RULETA[indice]
    val respondio = seleccionada != null
    val esCorrecto = seleccionada == alimento.aporte
    // Opciones barajadas en cada ronda para que cambien de posición
    val opcionesBarajadas = remember(indice) { alimento.opciones.shuffled() }

    // Animación: la ruleta crece mientras gira (zoom) y vuelve a su tamaño al detenerse
    LaunchedEffect(indice) {
        girando = true
        // Genera partículas de colores en el espacio vacío
        particulas = List(22) { ParticulaRuleta.aleatoria() }

        // Crece a 1.15x en el primer momento del giro
        escala.animateTo(
            targetValue = 1.15f,
            animationSpec = tween(durationMillis = 500)
        )

        val sector = 360f / ALIMENTOS_RULETA.size
        val actual = ((rotacion.value % 360f) + 360f) % 360f
        val deseado = ((240f - indice * sector) + 360f) % 360f
        var delta = deseado - actual
        if (delta <= 0) delta += 360f
        // Gira con frenado suave mientras mantiene el zoom
        rotacion.animateTo(
            targetValue = rotacion.value + delta + 360f * 3f,
            animationSpec = tween(durationMillis = 2800, easing = FastOutSlowInEasing)
        )
        // Al detenerse, vuelve al tamaño normal
        escala.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 400)
        )
        // Las partículas se van al detenerse
        particulas = emptyList()
        girando = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        ScreenHeader(titulo = "Rueda de la alimentación", onBack = onBack)

        Spacer(Modifier.height(12.dp))

        Text(
            text = "${indice + 1} de ${ALIMENTOS_RULETA.size}",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = InkSoft
        )
        LinearProgressIndicator(
            progress = { (indice + 1).toFloat() / ALIMENTOS_RULETA.size },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = Leaf,
            trackColor = LineColor
        )

        Spacer(Modifier.height(16.dp))

        // La ruleta
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp),
            contentAlignment = Alignment.Center
        ) {
            Ruleta(rotacion = rotacion.value, escala = escala.value)
        }

        // Partículas decorativas que suben desde abajo mientras gira la ruleta
        if (girando && particulas.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                particulas.forEach { p ->
                    ParticulaAnimada(particula = p)
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // El mensaje del alimento solo aparece cuando la ruleta se detiene
        if (!girando) {
            Text(
                text = "¡Cayó en ${alimento.emoji} ${alimento.nombre}!",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Ink,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "¿Qué aporta el ${alimento.nombre.lowercase()}?",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = InkSoft,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))
        }

        // Opciones
        if (!girando) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                opcionesBarajadas.forEach { opcion ->
                    OutlinedButton(
                        onClick = {
                            seleccionada = opcion
                            if (opcion == alimento.aporte) puntaje += 10
                        },
                        enabled = !respondio && !girando,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MangoDark),
                        border = BorderStroke(2.dp, if (opcion == alimento.aporte && respondio) Leaf else Mango),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(
                            text = opcion,
                            style = MaterialTheme.typography.labelLarge,
                            color = MangoDark
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Retroalimentación
        if (respondio) {
            Surface(
                color = if (esCorrecto) LeafLight else BerryLight,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (esCorrecto)
                        "✅ ¡Correcto! +10 puntos"
                    else
                        "❌ El ${alimento.nombre.lowercase()} aporta ${alimento.aporte}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (esCorrecto) LeafDark else Berry,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    if (indice + 1 >= ALIMENTOS_RULETA.size) {
                        onTerminada(puntaje)
                    } else {
                        indice++
                        seleccionada = null
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Leaf),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text(
                    text = if (indice + 1 >= ALIMENTOS_RULETA.size) "Ver resultados" else "Siguiente →",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White
                )
            }
        }
    }
}

/**
 * Ruleta: un solo Canvas dibuja los sectores de color y los emojis, girando
 * juntos (rotate). La flecha triangular queda fija arriba apuntando al disco.
 */
@Composable
private fun Ruleta(rotacion: Float, escala: Float) {
    val textMeasurer = rememberTextMeasurer()
    Box(
        modifier = Modifier
            .size(170.dp)
            .graphicsLayer {
                scaleX = escala
                scaleY = escala
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.size(170.dp)
        ) {
            val sector = 360f / ALIMENTOS_RULETA.size
            val radio = size.minDimension / 2f
            val centro = center

            rotate(degrees = rotacion, pivot = centro) {
                // Sectores de color
                ALIMENTOS_RULETA.forEachIndexed { i, _ ->
                    drawArc(
                        color = COLORES_RULETA[i],
                        startAngle = i * sector,
                        sweepAngle = sector,
                        useCenter = true,
                        topLeft = Offset(centro.x - radio, centro.y - radio),
                        size = Size(radio * 2, radio * 2)
                    )
                }
                // Emojis en el centro de cada sector (giran con la ruleta)
                ALIMENTOS_RULETA.forEachIndexed { i, item ->
                    val angulo = Math.toRadians((i * sector + sector / 2).toDouble())
                    val x = (centro.x + cos(angulo) * radio * 0.68).toFloat()
                    val y = (centro.y + sin(angulo) * radio * 0.68).toFloat()
                    val layout = textMeasurer.measure(
                        text = item.emoji,
                        style = TextStyle(fontSize = 22.sp)
                    )
                    drawText(
                        textLayoutResult = layout,
                        topLeft = Offset(
                            x - layout.size.width / 2f,
                            y - layout.size.height / 2f
                        )
                    )
                }
            }

            // Borde del disco (fijo)
            drawCircle(color = LeafDark, radius = radio, style = Stroke(4f))
            // Centro blanco (fijo)
            drawCircle(color = Color.White, radius = radio * 0.45f)
        }

        // Flecha indicadora arriba (fija, no gira)
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-6).dp)
                .size(width = 30.dp, height = 26.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val triangle = Path().apply {
                    moveTo(size.width / 2f, size.height)
                    lineTo(0f, 0f)
                    lineTo(size.width, 0f)
                    close()
                }
                drawPath(path = triangle, color = Mango)
            }
        }
    }
}

/** Partícula decorativa que sube desde abajo hacia la ruleta con su propia animación. */
@Composable
private fun ParticulaAnimada(particula: ParticulaRuleta) {
    // Cada partícula sube de forma independiente (duración y retraso propios)
    // Empieza desde abajo del todo (fuera de vista) y sube hasta la ruleta
    val offsetY = remember { Animatable(1f) } // 1 = abajo, 0 = arriba
    LaunchedEffect(particula) {
        offsetY.snapTo(1f)
        offsetY.animateTo(
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = particula.duracion,
                    delayMillis = particula.retraso,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            )
        )
    }
    Text(
        text = particula.emoji,
        fontSize = particula.tamano.sp,
        modifier = Modifier
            .offset(x = particula.x.dp)
            .offset { IntOffset(0, (offsetY.value * 400f - 260f).roundToInt()) }
    )
}

/** Partícula decorativa (emoji de comida) que sube mientras gira la ruleta. */
private data class ParticulaRuleta(
    val emoji: String,
    val x: Int,
    val duracion: Int,
    val retraso: Int,
    val tamano: Int
) {
    companion object {
        private val EMOJIS = listOf("⭐", "✨", "🍀", "🌸", "💚", "🧡", "💙", "❤️", "🟡", "🟢")
        fun aleatoria(): ParticulaRuleta {
            val r = Random
            return ParticulaRuleta(
                emoji = EMOJIS[r.nextInt(EMOJIS.size)],
                x = 10 + r.nextInt(270),       // posición horizontal
                duracion = 900 + r.nextInt(900), // 900-1800ms (independiente)
                retraso = r.nextInt(700),        // 0-700ms de retraso
                tamano = 10 + r.nextInt(8)       // 10-18sp (más pequeñas)
            )
        }
    }
}
