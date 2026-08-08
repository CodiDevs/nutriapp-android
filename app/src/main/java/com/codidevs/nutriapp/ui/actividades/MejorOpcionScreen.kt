package com.codidevs.nutriapp.ui.actividades

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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

private data class OpcionMejor(
    val emojis: String,
    val texto: String
)

private data class PreguntaMejor(
    val titulo: String,
    val opcionCorrecta: OpcionMejor,
    val opcionIncorrecta: OpcionMejor
)

/** Preguntas de "¿Cuál es la mejor opción?" del Módulo 1, según las indicaciones. */
private val PREGUNTAS_MEJOR = listOf(
    PreguntaMejor(
        titulo = "¿Qué elegirías para un desayuno saludable?",
        opcionCorrecta = OpcionMejor("🥛🍎🍞", "Leche, fruta y pan"),
        opcionIncorrecta = OpcionMejor("🥤🍬🍩", "Gaseosa y dulces")
    ),
    PreguntaMejor(
        titulo = "¿Qué merienda es mejor?",
        opcionCorrecta = OpcionMejor("🍌🥛", "Banano y yogur"),
        opcionIncorrecta = OpcionMejor("🍫🥤", "Chocolate y gaseosa")
    )
)

/**
 * Actividad "La mejor opción": el niño elige la opción más saludable entre dos.
 * Con aciertos suma puntos; al final llama a onTerminada(puntaje).
 */
@Composable
fun MejorOpcionScreen(
    onBack: () -> Unit,
    onTerminada: (puntaje: Int) -> Unit
) {
    var indice by remember { mutableStateOf(0) }
    var puntaje by remember { mutableStateOf(0) }
    var respuesta by remember { mutableStateOf<Boolean?>(null) } // true = correcta, false = incorrecta

    val pregunta = PREGUNTAS_MEJOR[indice]
    val respondio = respuesta != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        ScreenHeader(titulo = "La mejor opción", onBack = onBack)

        Spacer(Modifier.height(12.dp))

        Text(
            text = "${indice + 1} de ${PREGUNTAS_MEJOR.size}",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = InkSoft
        )
        LinearProgressIndicator(
            progress = { (indice + 1).toFloat() / PREGUNTAS_MEJOR.size },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = Leaf,
            trackColor = LineColor
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = pregunta.titulo,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Ink,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        // Las dos opciones
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            TarjetaOpcion(
                opcion = pregunta.opcionCorrecta,
                seleccionable = !respondio,
                correcta = respuesta == true,
                onClick = {
                    respuesta = true
                    puntaje += 10
                }
            )
            TarjetaOpcion(
                opcion = pregunta.opcionIncorrecta,
                seleccionable = !respondio,
                correcta = false,
                onClick = {
                    respuesta = false
                }
            )
        }

        Spacer(Modifier.height(20.dp))

        // Retroalimentación
        if (respondio) {
            Surface(
                color = if (respuesta == true) LeafLight else BerryLight,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (respuesta == true)
                        "✅ ¡Correcto! +10 puntos"
                    else
                        "❌ La mejor opción era: ${pregunta.opcionCorrecta.emojis} ${pregunta.opcionCorrecta.texto}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (respuesta == true) LeafDark else Berry,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    if (indice + 1 >= PREGUNTAS_MEJOR.size) {
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
                    text = if (indice + 1 >= PREGUNTAS_MEJOR.size) "Ver resultados" else "Siguiente →",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun TarjetaOpcion(
    opcion: OpcionMejor,
    seleccionable: Boolean,
    correcta: Boolean,
    onClick: () -> Unit
) {
    Surface(
        color = when {
            correcta -> LeafLight
            seleccionable -> Color.White
            else -> Color.White
        },
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
            when {
                correcta -> 3.dp
                else -> 2.dp
            },
            when {
                correcta -> Leaf
                else -> LineColor
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickableHabilitado(seleccionable, onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = opcion.emojis, fontSize = 40.sp)
            Spacer(Modifier.height(8.dp))
            Text(
                text = opcion.texto,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Ink,
                textAlign = TextAlign.Center
            )
            if (correcta) {
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "✓ Correcta",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = LeafDark
                )
            }
        }
    }
}

@Composable
private fun Modifier.clickableHabilitado(habilitado: Boolean, onClick: () -> Unit): Modifier =
    if (habilitado) {
        this.then(
            Modifier.clickable(onClick = onClick)
        )
    } else {
        this
    }
