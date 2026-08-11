package com.codidevs.nutriapp.ui.actividades

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import com.codidevs.nutriapp.data.models.GrupoAlimenticio
import com.codidevs.nutriapp.ui.components.AlimentoFigura
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
 * Actividad "¿A qué grupo pertenece?": arrastra la tarjeta del alimento con el dedo
 * hasta el grupo correcto. Al pasar sobre un grupo se resalta; al soltar valida.
 */
@Composable
fun GrupoPerteneceScreen(
    grupos: List<GrupoAlimenticio>,
    onBack: () -> Unit,
    onTerminada: (puntaje: Int) -> Unit
) {
    // Rondas: 6 alimentos al azar, cada uno con su grupo correcto + 3 distractores
    val rondas = remember(grupos) {
        val alimentos = grupos.flatMap { g -> g.alimentos.map { a -> a to g.nombre } }
            .shuffled()
            .take(6)
        alimentos.map { (alimento, grupoCorrecto) ->
            val correcto = grupos.first { it.nombre == grupoCorrecto }
            val otros = grupos.filter { it.nombre != grupoCorrecto }.shuffled().take(3)
            Triple(alimento, correcto, (otros + correcto).shuffled())
        }
    }

    var indice by remember { mutableStateOf(0) }
    var puntaje by remember { mutableStateOf(0) }
    var retro by remember { mutableStateOf<String?>(null) }
    // Grupo donde se soltó la figura con su resultado ("correcto"/"incorrecto")
    var grupoResultado by remember { mutableStateOf<Pair<String, String>?>(null) }

    // Arrastre: todo se mide en coordenadas GLOBALES de pantalla (positionInRoot)
    // para que tarjeta, grupos y el punto del dedo estén en el mismo sistema.
    var posBoxRaiz by remember { mutableStateOf(Offset.Zero) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    var inicioDrag by remember { mutableStateOf<Offset?>(null) }
    var ultimoPunto by remember { mutableStateOf<Offset?>(null) }
    var arrastrando by remember { mutableStateOf(false) }
    var rectTarjeta by remember { mutableStateOf(Rect.Zero) }
    var zonaSobre by remember { mutableStateOf<String?>(null) }
    // Mapa fresco por ronda: se llena en el layout con onGloballyPositioned.
    // NO se limpia con efectos asíncronos: eso dejaba el mapa vacío en el
    // primer arrastre de cada ronda (fallaba la primera vez, funcionaba la segunda).
    val posicionesGrupo = remember(indice) { mutableMapOf<String, Rect>() }

    val ronda = rondas[indice]
    val alimento = ronda.first
    val gruposRonda = ronda.third

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { posBoxRaiz = it.positionInRoot() }
            .pointerInput(alimento, indice, posBoxRaiz) {
                detectDragGestures(
                    onDragStart = { pos ->
                        // Solo se arrastra si aún no ha respondido (retro == null)
                        if (retro == null) {
                            // Convierte el punto (local del Box) a global de pantalla
                            val puntoGlobal = posBoxRaiz + pos
                            if (rectTarjeta.contains(puntoGlobal)) {
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
                            // Resalta el grupo más cercano al dedo (tolerante)
                            zonaSobre = grupoMasCercano(puntoGlobal, posicionesGrupo)
                        }
                    },
                    onDragEnd = {
                        if (arrastrando) {
                            val punto = ultimoPunto ?: (inicioDrag ?: Offset.Zero)
                            val grupoSoltado = grupoMasCercano(punto, posicionesGrupo)
                            when (grupoSoltado) {
                                null -> {
                                    // Soltó lejos de cualquier grupo: la figura vuelve a su lugar
                                    dragOffset = Offset.Zero
                                    grupoResultado = null
                                }
                                ronda.second.nombre -> {
                                    puntaje += 10
                                    retro = "correcto"
                                    // La figura se queda donde la soltó (sobre el grupo correcto)
                                    dragOffset = punto - (inicioDrag ?: punto)
                                    grupoResultado = grupoSoltado to "correcto"
                                }
                                else -> {
                                    retro = "incorrecto"
                                    // La figura se queda donde la soltó (sobre el grupo equivocado)
                                    dragOffset = punto - (inicioDrag ?: punto)
                                    grupoResultado = grupoSoltado to "incorrecto"
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
            ScreenHeader(titulo = "¿A qué grupo pertenece?", onBack = onBack)

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
                text = "Arrastra el alimento hasta su grupo",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = Ink,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(12.dp))

            // Tarjeta del alimento (arrastrable): emoji + nombre
            // El Box contenedor tiene zIndex alto para que la figura quede encima de los grupos
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
                        .size(96.dp)
                        .offset { IntOffset(dragOffset.x.roundToInt(), dragOffset.y.roundToInt()) }
                        .onGloballyPositioned { rectTarjeta = it.boundsInRoot() }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        AlimentoFigura(
                            nombre = alimento.nombre,
                            emoji = alimento.emoji,
                            tamano = 40
                        )
                        Text(
                            text = alimento.nombre,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = InkSoft
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Grupos posibles (2x2)
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                gruposRonda.chunked(2).forEach { fila ->
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        fila.forEach { grupo ->
                            ZonaGrupo(
                                grupo = grupo,
                                resaltada = zonaSobre == grupo.nombre,
                                resultado = grupoResultado?.takeIf { it.first == grupo.nombre }?.second,
                                modifier = Modifier.weight(1f),
                                onPosicion = { nombre, rect -> posicionesGrupo[nombre] = rect }
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
                            "❌ Casi. ${alimento.emoji} ${alimento.nombre} es de ${ronda.second.emoji} ${ronda.second.nombre}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (estado == "correcto") LeafDark else Berry,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Spacer(Modifier.height(14.dp))
            }

            // Botón siguiente / terminar
            if (retro != null) {
                Button(
                    onClick = com.codidevs.nutriapp.data.audio.onClickConSonido {
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
                        text = if (indice + 1 >= rondas.size) "Terminar" else "Siguiente →",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun ZonaGrupo(
    grupo: GrupoAlimenticio,
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
            .height(96.dp)
            .onGloballyPositioned { coords ->
                onPosicion(grupo.nombre, coords.boundsInRoot())
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = grupo.emoji, fontSize = 30.sp)
            Spacer(Modifier.height(4.dp))
            Text(
                text = grupo.nombre,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = if (resultado != null) {
                    if (resultado == "correcto") LeafDark else Berry
                } else if (resaltada) LeafDark else InkSoft,
                textAlign = TextAlign.Center
            )
        }
    }
}

/** Devuelve el grupo más cercano al punto, o null si está demasiado lejos de todos. */
private fun grupoMasCercano(
    punto: Offset,
    posiciones: Map<String, Rect>
): String? {
    val mejor = posiciones.entries
        .map { (nombre, rect) ->
            val cx = rect.left + rect.width / 2
            val cy = rect.top + rect.height / 2
            val dist = kotlin.math.sqrt(
                (punto.x - cx) * (punto.x - cx) +
                    (punto.y - cy) * (punto.y - cy)
            )
            nombre to dist
        }
        .minByOrNull { it.second }
        ?: return null
    // Umbral: si está muy lejos de todo, se considera que soltó fuera
    return if (mejor.second < 400f) mejor.first else null
}
