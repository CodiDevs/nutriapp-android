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

data class PreguntaQuiz(
    val pregunta: String,
    val correcta: String,
    val incorrectas: List<String>
)

/**
 * Actividad "Quiz": pregunta de opción múltiple (1 correcta + 3 incorrectas).
 * Se usa en el Nivel 4 (¿Cuál es la respuesta correcta?).
 */
@Composable
fun QuizScreen(
    preguntas: List<PreguntaQuiz>,
    onBack: () -> Unit,
    onTerminada: (puntaje: Int) -> Unit
) {
    var indice by remember { mutableStateOf(0) }
    var puntaje by remember { mutableStateOf(0) }
    var seleccionada by remember { mutableStateOf<String?>(null) }

    val pregunta = preguntas[indice]
    val respondio = seleccionada != null
    val esCorrecto = seleccionada == pregunta.correcta
    val opciones = remember(indice) { (pregunta.incorrectas + pregunta.correcta).shuffled() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        ScreenHeader(titulo = "¿Cuál es la respuesta correcta?", onBack = onBack)

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

        Spacer(Modifier.height(24.dp))

        Text(
            text = pregunta.pregunta,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Ink,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            opciones.forEach { opcion ->
                Surface(
                    color = when {
                        respondio && opcion == pregunta.correcta -> LeafLight
                        respondio && opcion == seleccionada -> BerryLight
                        else -> Color.White
                    },
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(
                        when {
                            respondio && opcion == pregunta.correcta -> 3.dp
                            respondio && opcion == seleccionada -> 3.dp
                            else -> 2.dp
                        },
                        when {
                            respondio && opcion == pregunta.correcta -> Leaf
                            respondio && opcion == seleccionada -> Berry
                            else -> LineColor
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = !respondio) {
                            seleccionada = opcion
                            if (opcion == pregunta.correcta) puntaje += 10
                        }
                ) {
                    Text(
                        text = opcion,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = Ink,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp, horizontal = 12.dp)
                    )
                }
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
                        "❌ La respuesta correcta era: ${pregunta.correcta}",
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
                    if (indice + 1 >= preguntas.size) {
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
                    text = if (indice + 1 >= preguntas.size) "Ver resultados" else "Siguiente →",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White
                )
            }
        }
    }
}
