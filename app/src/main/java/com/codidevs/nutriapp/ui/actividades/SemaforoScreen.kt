package com.codidevs.nutriapp.ui.actividades

import androidx.compose.foundation.BorderStroke
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
import com.codidevs.nutriapp.ui.theme.MangoLight

data class AlimentoSemaforo(
    val emoji: String,
    val nombre: String
)

data class SemaforoDatos(
    val verde: List<AlimentoSemaforo>,
    val amarillo: List<AlimentoSemaforo>,
    val rojo: List<AlimentoSemaforo>
)

/**
 * Actividad "El Semáforo Saludable": el niño clasifica cada alimento en
 * 🟢 (todos los días), 🟡 (con moderación) o 🔴 (solo de vez en cuando).
 */
@Composable
fun SemaforoScreen(
    datos: SemaforoDatos,
    onBack: () -> Unit,
    onTerminada: (puntaje: Int) -> Unit
) {
    // Lista de alimentos mezclada con su categoría correcta
    val alimentos = remember(datos) {
        (datos.verde.map { AlimentoConCategoria(it, "verde") } +
            datos.amarillo.map { AlimentoConCategoria(it, "amarillo") } +
            datos.rojo.map { AlimentoConCategoria(it, "rojo") }).shuffled()
    }

    var indice by remember { mutableStateOf(0) }
    var puntaje by remember { mutableStateOf(0) }
    var respuesta by remember { mutableStateOf<String?>(null) } // categoria elegida

    val actual = alimentos[indice]
    val respondio = respuesta != null
    val esCorrecto = respuesta == actual.categoria

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        ScreenHeader(titulo = "El Semáforo Saludable", onBack = onBack)

        Spacer(Modifier.height(12.dp))

        Text(
            text = "${indice + 1} de ${alimentos.size}",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = InkSoft
        )
        LinearProgressIndicator(
            progress = { (indice + 1).toFloat() / alimentos.size },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = Leaf,
            trackColor = LineColor
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = "¿Dónde va este alimento?",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = Ink,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        // El alimento
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(2.dp, LineColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = actual.alimento.emoji, fontSize = 56.sp)
                Spacer(Modifier.height(8.dp))
                Text(
                    text = actual.alimento.nombre,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Ink
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // Opciones del semáforo
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            OpcionSemaforo("🟢", "Todos los días", "verde", Leaf, respondio, esCorrecto, respuesta) {
                respuesta = "verde"
                if (actual.categoria == "verde") puntaje += 10
            }
            OpcionSemaforo("🟡", "Con moderación", "amarillo", Mango, respondio, esCorrecto, respuesta) {
                respuesta = "amarillo"
                if (actual.categoria == "amarillo") puntaje += 10
            }
            OpcionSemaforo("🔴", "Solo de vez en cuando", "rojo", Berry, respondio, esCorrecto, respuesta) {
                respuesta = "rojo"
                if (actual.categoria == "rojo") puntaje += 10
            }
        }

        Spacer(Modifier.height(20.dp))

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
                        "❌ ${actual.alimento.nombre} es para ${textoCategoria(actual.categoria)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (esCorrecto) LeafDark else Berry,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = com.codidevs.nutriapp.data.audio.onClickConSonido {
                    if (indice + 1 >= alimentos.size) {
                        onTerminada(puntaje)
                    } else {
                        indice++
                        respuesta = null
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Leaf),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text(
                    text = if (indice + 1 >= alimentos.size) "Ver resultados" else "Siguiente →",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun OpcionSemaforo(
    emoji: String,
    etiqueta: String,
    categoria: String,
    color: Color,
    respondio: Boolean,
    esCorrecto: Boolean,
    respuesta: String?,
    onClick: (String) -> Unit
) {
    Surface(
        color = when {
            respondio && categoria == respuesta -> if (esCorrecto) LeafLight else BerryLight
            respondio && categoria != respuesta && esCorrecto && categoria == respuesta -> LeafLight
            else -> Color.White
        },
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(
            when {
                respondio && categoria == respuesta -> 3.dp
                else -> 2.dp
            },
            when {
                respondio && categoria == respuesta -> if (esCorrecto) Leaf else Berry
                else -> LineColor
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickableSinRipple(enabled = !respondio) {
                com.codidevs.nutriapp.data.audio.SoundManager.click()
                onClick(categoria)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = emoji, fontSize = 24.sp)
            Spacer(Modifier.width(12.dp))
            Text(
                text = etiqueta,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Ink
            )
        }
    }
}

private fun textoCategoria(cat: String): String = when (cat) {
    "verde" -> "todos los días"
    "amarillo" -> "con moderación"
    else -> "solo de vez en cuando"
}

/** Alimento con su categoría del semáforo (verde/amarillo/rojo). */
private data class AlimentoConCategoria(
    val alimento: AlimentoSemaforo,
    val categoria: String
)

private fun Modifier.clickableSinRipple(enabled: Boolean, onClick: () -> Unit): Modifier =
    if (enabled) this.then(Modifier.clickable(onClick = onClick)) else this
