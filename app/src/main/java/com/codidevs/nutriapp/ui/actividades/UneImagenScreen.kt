package com.codidevs.nutriapp.ui.actividades

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.codidevs.nutriapp.data.models.ItemDato
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
import kotlin.math.roundToInt

/**
 * Actividad "Une la imagen con el beneficio": arrastra la imagen (ej. 🚴)
 * hasta el beneficio correcto (ej. "Fortalece el corazón").
 */
@Composable
fun UneImagenScreen(
    pares: List<ItemDato>,
    onBack: () -> Unit,
    onTerminada: (puntaje: Int) -> Unit
) {
    // Rondas: se muestran de a una, con 4 beneficios posibles (1 correcto + 3 distractores)
    val rondas = remember(pares) {
        val beneficios = pares.map { it.texto }
        pares.map { par ->
            val distractores = beneficios.filter { it != par.texto }.shuffled().take(3)
            RondaUne(par, (distractores + par.texto).shuffled())
        }
    }

    var indice by remember { mutableStateOf(0) }
    var puntaje by remember { mutableStateOf(0) }
    var retro by remember { mutableStateOf<String?>(null) }
    // Beneficio donde se soltó la figura con su resultado ("correcto"/"incorrecto")
    var grupoResultado by remember { mutableStateOf<Pair<String, String>?>(null) }

    // Estado del arrastre (coordenadas globales)
    var posBoxRaiz by remember { mutableStateOf(Offset.Zero) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    var inicioDrag by remember { mutableStateOf<Offset?>(null) }
    var ultimoPunto by remember { mutableStateOf<Offset?>(null) }
    var arrastrando by remember { mutableStateOf(false) }
    var rectImagen by remember { mutableStateOf(Rect.Zero) }
    var zonaSobre by remember { mutableStateOf<String?>(null) }
    // Mapa fresco por ronda
    val posicionesBeneficio = remember(indice) { mutableMapOf<String, Rect>() }

    val ronda = rondas[indice]
    val imagen = ronda.imagen
    val beneficios = ronda.beneficios

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { posBoxRaiz = it.positionInRoot() }
            .pointerInput(indice, posBoxRaiz) {
                detectDragGestures(
                    onDragStart = { pos ->
                        // Solo se arrastra si aún no ha respondido
                        if (retro == null) {
                            val puntoGlobal = posBoxRaiz + pos
                            if (rectImagen.contains(puntoGlobal)) {
                                inicioDrag = puntoGlobal
                                ultimoPunto = puntoGlobal
                                arrastrando = true
                            }
                        }
                    },
                    onDrag = { change, _ ->
                        if (arrastrando) {
                            change.consume()
                            val puntoGlobal = posBoxRaiz + change.position
                            dragOffset = puntoGlobal - (inicioDrag ?: puntoGlobal)
                            ultimoPunto = puntoGlobal
                            zonaSobre = beneficioMasCercano(puntoGlobal, posicionesBeneficio)
                        }
                    },
                    onDragEnd = {
                        if (arrastrando) {
                            val punto = ultimoPunto ?: (inicioDrag ?: Offset.Zero)
                            val soltado = beneficioMasCercano(punto, posicionesBeneficio)
                            when (soltado) {
                                null -> {
                                    // Soltó lejos: la figura vuelve
                                    dragOffset = Offset.Zero
                                    grupoResultado = null
                                }
                                imagen.texto -> {
                                    puntaje += 10
                                    retro = "correcto"
                                    // La figura se queda donde la soltó
                                    dragOffset = punto - (inicioDrag ?: punto)
                                    grupoResultado = soltado to "correcto"
                                }
                                else -> {
                                    retro = "incorrecto"
                                    // La figura se queda donde la soltó
                                    dragOffset = punto - (inicioDrag ?: punto)
                                    grupoResultado = soltado to "incorrecto"
                                }
                            }
                        }
                        arrastrando = false
                        inicioDrag = null
                        ultimoPunto = null
                        zonaSobre = null
                    },
                    onDragCancel = {
                        arrastrando = false
                        inicioDrag = null
                        ultimoPunto = null
                        dragOffset = Offset.Zero
                        zonaSobre = null
                    }
                )
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            ScreenHeader(titulo = "Une la imagen con el beneficio", onBack = onBack)

            Spacer(Modifier.height(12.dp))

            Text(
                text = "${indice + 1} de ${rondas.size}",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = InkSoft
            )
            LinearProgressIndicator(
                progress = { (indice + 1).toFloat() / rondas.size },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = Leaf,
                trackColor = LineColor
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Arrastra la imagen hasta su beneficio",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = Ink,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            // Imagen arrastrable (zIndex alto para quedar encima de las opciones)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .zIndex(10f),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(2.dp, if (retro == null) Leaf else LineColor),
                    shadowElevation = 6.dp,
                    modifier = Modifier
                        .size(110.dp)
                        .offset { IntOffset(dragOffset.x.roundToInt(), dragOffset.y.roundToInt()) }
                        .onGloballyPositioned { rectImagen = it.boundsInRoot() }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(text = imagen.emoji, fontSize = 44.sp)
                        if (imagen.nombre.isNotEmpty()) {
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = imagen.nombre,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = InkSoft
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Beneficios posibles (2x2)
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                beneficios.chunked(2).forEach { fila ->
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        fila.forEach { beneficio ->
                            BeneficioTarjeta(
                                texto = beneficio,
                                resaltada = zonaSobre == beneficio,
                                resultado = grupoResultado?.takeIf { it.first == beneficio }?.second,
                                modifier = Modifier.weight(1f),
                                onPosicion = { texto, rect -> posicionesBeneficio[texto] = rect }
                            )
                        }
                        if (fila.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            // Retroalimentación
            retro?.let { estado ->
                Surface(
                    color = if (estado == "correcto") LeafLight else BerryLight,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (estado == "correcto")
                            "✅ ¡Correcto! +10 puntos"
                        else
                            "❌ ${imagen.emoji} ${imagen.texto}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (estado == "correcto") LeafDark else Berry,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Spacer(Modifier.height(14.dp))

                Button(
                    onClick = {
                        if (indice + 1 >= rondas.size) {
                            onTerminada(puntaje)
                        } else {
                            indice++
                            retro = null
                            grupoResultado = null
                            dragOffset = Offset.Zero // la figura vuelve a su lugar en la nueva ronda
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Leaf),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp)
                ) {
                    Text(
                        text = if (indice + 1 >= rondas.size) "Ver resultados" else "Siguiente →",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun BeneficioTarjeta(
    texto: String,
    resaltada: Boolean,
    resultado: String?, // "correcto" | "incorrecto" | null
    modifier: Modifier = Modifier,
    onPosicion: (String, Rect) -> Unit
) {
    val colorFondo = when (resultado) {
        "correcto" -> LeafLight      // verde pastel
        "incorrecto" -> BerryLight   // rojo pastel
        else -> if (resaltada) LeafLight else Color.White
    }
    val colorBorde = when (resultado) {
        "correcto" -> Leaf
        "incorrecto" -> Berry
        else -> if (resaltada) Mango else LineColor
    }
    Surface(
        color = colorFondo,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(if (resaltada || resultado != null) 3.dp else 2.dp, colorBorde),
        modifier = modifier
            .height(90.dp)
            .onGloballyPositioned { coords ->
                onPosicion(texto, coords.boundsInRoot())
            }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = texto,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = when (resultado) {
                    "correcto" -> LeafDark
                    "incorrecto" -> Berry
                    else -> if (resaltada) LeafDark else Ink
                },
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
        }
    }
}

/** Devuelve el beneficio más cercano al punto, o null si está lejos de todos. */
private fun beneficioMasCercano(
    punto: Offset,
    posiciones: Map<String, Rect>
): String? {
    val mejor = posiciones.entries
        .map { (texto, rect) ->
            val cx = rect.left + rect.width / 2
            val cy = rect.top + rect.height / 2
            val dist = kotlin.math.sqrt(
                (punto.x - cx) * (punto.x - cx) +
                    (punto.y - cy) * (punto.y - cy)
            )
            texto to dist
        }
        .minByOrNull { it.second }
        ?: return null
    return if (mejor.second < 400f) mejor.first else null
}

/** Una ronda: la imagen a arrastrar y los beneficios posibles. */
private data class RondaUne(
    val imagen: ItemDato,
    val beneficios: List<String>
)
