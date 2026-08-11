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
import com.codidevs.nutriapp.data.models.MejorOpcionNivel2
import com.codidevs.nutriapp.ui.components.ScreenHeader
import com.codidevs.nutriapp.ui.theme.Berry
import com.codidevs.nutriapp.ui.theme.BerryLight
import com.codidevs.nutriapp.ui.theme.Ink
import com.codidevs.nutriapp.ui.theme.InkSoft
import com.codidevs.nutriapp.ui.theme.Leaf
import com.codidevs.nutriapp.ui.theme.LeafDark
import com.codidevs.nutriapp.ui.theme.LeafLight
import com.codidevs.nutriapp.ui.theme.LineColor

/**
 * Actividad "La mejor opción" (Nivel 2): elige la opción con el nutriente correcto.
 */
@Composable
fun MejorOpcionNivel2Screen(
    preguntas: List<MejorOpcionNivel2>,
    onBack: () -> Unit,
    onTerminada: (puntaje: Int) -> Unit
) {
    var indice by remember { mutableStateOf(0) }
    var puntaje by remember { mutableStateOf(0) }
    var respuesta by remember { mutableStateOf<Boolean?>(null) }

    val pregunta = preguntas[indice]
    val respondio = respuesta != null
    // Opciones barajadas
    val opciones = remember(indice) {
        listOf(
            Triple(pregunta.emojiCorrecta, pregunta.textoCorrecta, true),
            Triple(pregunta.emojiIncorrecta, pregunta.textoIncorrecta, false)
        ).shuffled()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        ScreenHeader(titulo = "La mejor opción", onBack = onBack)

        Spacer(Modifier.height(12.dp))

        Text(
            text = "${indice + 1} de ${preguntas.size}",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = InkSoft
        )
        LinearProgressIndicator(
            progress = { (indice + 1).toFloat() / preguntas.size },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = Leaf,
            trackColor = LineColor
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = pregunta.pregunta,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Ink,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            opciones.forEach { (emoji, texto, esCorrecta) ->
                Surface(
                    color = when {
                        respondio && esCorrecta -> LeafLight
                        respondio && !esCorrecta -> Color.White
                        else -> Color.White
                    },
                    shape = RoundedCornerShape(18.dp),
                    border = BorderStroke(
                        when {
                            respondio && esCorrecta -> 3.dp
                            else -> 2.dp
                        },
                        when {
                            respondio && esCorrecta -> Leaf
                            else -> LineColor
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = !respondio) {
                            com.codidevs.nutriapp.data.audio.SoundManager.click()
                            respuesta = esCorrecta
                            if (esCorrecta) puntaje += 10
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = emoji, fontSize = 40.sp)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = texto,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Ink,
                            textAlign = TextAlign.Center
                        )
                        if (respondio && esCorrecta) {
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
        }

        Spacer(Modifier.height(20.dp))

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
                        "❌ La mejor opción era: ${pregunta.emojiCorrecta} ${pregunta.textoCorrecta}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (respuesta == true) LeafDark else Berry,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = com.codidevs.nutriapp.data.audio.onClickConSonido {
                    if (indice + 1 >= preguntas.size) {
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
                    text = if (indice + 1 >= preguntas.size) "Ver resultados" else "Siguiente →",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White
                )
            }
        }
    }
}
