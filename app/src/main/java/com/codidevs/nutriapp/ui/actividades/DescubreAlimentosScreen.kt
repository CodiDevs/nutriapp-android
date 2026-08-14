package com.codidevs.nutriapp.ui.actividades

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codidevs.nutriapp.data.models.Alimento
import com.codidevs.nutriapp.ui.components.BarraProgresoActividad
import com.codidevs.nutriapp.ui.components.DecoracionFondoActividad
import com.codidevs.nutriapp.ui.components.ScreenHeader
import com.codidevs.nutriapp.ui.components.pulsoAnimado
import com.codidevs.nutriapp.ui.theme.*

/**
 * Actividad "Descubre los alimentos": el niño toca la tarjeta del alimento
 * y se revela qué hace en su cuerpo.
 */
@Composable
fun DescubreAlimentosScreen(
    alimentos: List<Alimento>,
    onBack: () -> Unit,
    onTerminada: (puntaje: Int) -> Unit
) {
    var indice by remember { mutableStateOf(0) }
    var revelado by remember { mutableStateOf(false) }

    val alimento = alimentos[indice]

    Box(modifier = Modifier.fillMaxSize().background(BgApp)) {
        DecoracionFondoActividad()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            ScreenHeader(titulo = "Descubre los alimentos", onBack = onBack)

            Spacer(Modifier.height(12.dp))

            BarraProgresoActividad(actual = indice + 1, total = alimentos.size)

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Toca la tarjeta para descubrir qué hace este alimento",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = Ink,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            Card(
                onClick = {
                    com.codidevs.nutriapp.data.audio.SoundManager.click()
                    revelado = true
                },
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(2.dp, if (revelado) Leaf else LineColor),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .pulsoAnimado(enabled = !revelado)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 36.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = alimento.emoji, fontSize = 64.sp)
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = alimento.nombre,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Ink
                    )

                    Spacer(Modifier.height(16.dp))

                    if (revelado) {
                        Text(
                            text = "✅ ¡Descubierto! +10 puntos",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = LeafDark
                        )
                        Spacer(Modifier.height(8.dp))
                        Surface(
                            color = LeafLight,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                        ) {
                            Text(
                                text = alimento.beneficio,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = LeafDark,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    } else {
                        Text(
                            text = "❓",
                            fontSize = 30.sp,
                            color = InkSoft
                        )
                    }
                }
            }

            Spacer(Modifier.height(28.dp))

            Button(
                onClick = com.codidevs.nutriapp.data.audio.onClickConSonido {
                    if (indice + 1 >= alimentos.size) {
                        onTerminada(10)
                    } else {
                        indice++
                        revelado = false
                    }
                },
                enabled = revelado,
                colors = ButtonDefaults.buttonColors(containerColor = Leaf),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .pulsoAnimado(enabled = revelado)
            ) {
                Text(
                    text = if (indice + 1 >= alimentos.size) "Terminar" else "Siguiente →",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White
                )
            }
        }
    }
}
