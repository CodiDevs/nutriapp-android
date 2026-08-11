package com.codidevs.nutriapp.ui.actividades

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
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

data class FraseIncompleta(
    val emoji: String,
    val fraseAntes: String,
    val fraseDespues: String,
    val respuesta: String,
    val opciones: List<String>
)

/**
 * Actividad "Completa la frase": el niño toca la tarjeta con la palabra correcta
 * y se coloca en el espacio de la oración; luego se valida.
 */
@Composable
fun CompletaFraseScreen(
    frases: List<FraseIncompleta>,
    onBack: () -> Unit,
    onTerminada: (puntaje: Int) -> Unit
) {
    var indice by remember { mutableStateOf(0) }
    var puntaje by remember { mutableStateOf(0) }
    var seleccionada by remember { mutableStateOf<String?>(null) }

    val frase = frases[indice]
    val opcionesBarajadas = remember(indice) { frase.opciones.shuffled() }
    val esCorrecto = seleccionada == frase.respuesta

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        ScreenHeader(titulo = "Completa la frase", onBack = onBack)

        Spacer(Modifier.height(12.dp))

        Text(
            text = "${indice + 1} de ${frases.size}",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = InkSoft
        )
        LinearProgressIndicator(
            progress = { (indice + 1).toFloat() / frases.size },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = Leaf,
            trackColor = LineColor
        )

        Spacer(Modifier.height(20.dp))

        // Tarjeta con la frase y el espacio
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(2.dp, LineColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = frase.emoji, fontSize = 56.sp)
                Spacer(Modifier.height(16.dp))

                val blancoColor = when {
                    seleccionada == null -> InkSoft
                    esCorrecto -> LeafDark
                    else -> Berry
                }
                val blancoTexto = seleccionada ?: "_______"

                val anotado = buildAnnotatedString {
                    withStyle(SpanStyle(color = Ink, fontWeight = FontWeight.Bold)) {
                        append("${frase.fraseAntes} ")
                    }
                    withStyle(
                        SpanStyle(
                            color = blancoColor,
                            fontWeight = FontWeight.ExtraBold,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append(blancoTexto)
                    }
                    withStyle(SpanStyle(color = Ink, fontWeight = FontWeight.Bold)) {
                        append(" ${frase.fraseDespues}")
                    }
                }
                Text(
                    text = anotado,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        if (seleccionada == null) {
            // Opciones de respuesta (tarjetas)
            Text(
                text = "Toca la palabra correcta:",
                style = MaterialTheme.typography.bodyMedium,
                color = InkSoft,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                opcionesBarajadas.forEach { opcion ->
                    OutlinedButton(
                        onClick = com.codidevs.nutriapp.data.audio.onClickConSonido {
                            seleccionada = opcion
                            if (opcion == frase.respuesta) puntaje += 10
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MangoDark),
                        border = BorderStroke(2.dp, Mango),
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
        } else {
            // Retroalimentación
            Surface(
                color = if (esCorrecto) LeafLight else BerryLight,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (esCorrecto)
                        "✅ ¡Correcto! +10 puntos"
                    else
                        "❌ La respuesta correcta era: \"${frase.respuesta}\"",
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
                    if (indice + 1 >= frases.size) {
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
                    text = if (indice + 1 >= frases.size) "Ver resultados" else "Siguiente →",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White
                )
            }
        }
    }
}
