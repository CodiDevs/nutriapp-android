package com.codidevs.nutriapp.ui.actividades

import androidx.compose.foundation.BorderStroke
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
import com.codidevs.nutriapp.data.models.Nutriente
import com.codidevs.nutriapp.ui.components.ScreenHeader
import com.codidevs.nutriapp.ui.theme.Ink
import com.codidevs.nutriapp.ui.theme.InkSoft
import com.codidevs.nutriapp.ui.theme.Leaf
import com.codidevs.nutriapp.ui.theme.LeafDark
import com.codidevs.nutriapp.ui.theme.LeafLight
import com.codidevs.nutriapp.ui.theme.LineColor

/**
 * Actividad "Descubre los nutrientes" (Nivel 2): toca la tarjeta del nutriente
 * y se revela qué hace en tu cuerpo y en qué alimentos está.
 */
@Composable
fun DescubreNutrientesScreen(
    nutrientes: List<Nutriente>,
    onBack: () -> Unit,
    onTerminada: (puntaje: Int) -> Unit
) {
    var indice by remember { mutableStateOf(0) }
    var revelado by remember { mutableStateOf(false) }

    val nutriente = nutrientes[indice]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        ScreenHeader(titulo = "Descubre los nutrientes", onBack = onBack)

        Spacer(Modifier.height(12.dp))

        Text(
            text = "${indice + 1} de ${nutrientes.size}",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = InkSoft
        )
        LinearProgressIndicator(
            progress = { (indice + 1).toFloat() / nutrientes.size },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = Leaf,
            trackColor = LineColor
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = "Toca la tarjeta para descubrir este nutriente",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = Ink,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(20.dp))

        // Tarjeta del nutriente
        Card(
            onClick = { revelado = true },
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(2.dp, if (revelado) Leaf else LineColor),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 36.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = nutriente.emoji, fontSize = 64.sp)
                Spacer(Modifier.height(12.dp))
                Text(
                    text = nutriente.nombre,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Ink
                )

                Spacer(Modifier.height(16.dp))

                if (revelado) {
                    Surface(
                        color = LeafLight,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = nutriente.beneficio,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = LeafDark,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "Está en: ${nutriente.ejemplos.joinToString(", ")}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = InkSoft,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    Text(text = "❓", fontSize = 30.sp, color = InkSoft)
                }
            }
        }

        Spacer(Modifier.height(28.dp))

        Button(
            onClick = {
                if (indice + 1 >= nutrientes.size) {
                    onTerminada(10)
                } else {
                    indice++
                    revelado = false
                }
            },
            enabled = revelado,
            colors = ButtonDefaults.buttonColors(containerColor = Leaf),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().height(52.dp)
        ) {
            Text(
                text = if (indice + 1 >= nutrientes.size) "Terminar" else "Siguiente →",
                style = MaterialTheme.typography.labelLarge,
                color = Color.White
            )
        }
    }
}
