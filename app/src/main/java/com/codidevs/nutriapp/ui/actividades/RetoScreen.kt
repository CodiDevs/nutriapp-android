package com.codidevs.nutriapp.ui.actividades

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codidevs.nutriapp.ui.components.DecoracionFondoActividad
import com.codidevs.nutriapp.ui.components.ScreenHeader
import com.codidevs.nutriapp.ui.components.pulsoAnimado
import com.codidevs.nutriapp.ui.theme.*

data class AccionReto(
    val emoji: String,
    val accion: String,
    val puntos: Int
)

@Composable
fun RetoScreen(
    acciones: List<AccionReto>,
    onBack: () -> Unit,
    onTerminada: (puntaje: Int) -> Unit
) {
    var completadas by remember { mutableStateOf<Set<Int>>(emptySet()) }
    var puntaje by remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxSize().background(BgApp)) {
        DecoracionFondoActividad()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            ScreenHeader(titulo = "Mi reto saludable", onBack = onBack)

            Spacer(Modifier.height(12.dp))

            Text(text = "Toca cada acción que cumpliste hoy. ¡Cada una te da ⭐!", style = MaterialTheme.typography.bodyMedium, color = InkSoft, fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                acciones.forEachIndexed { index, accion ->
                    val hecha = index in completadas
                    Surface(
                        color = if (hecha) LeafLight else Color.White,
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(2.dp, if (hecha) Leaf else LineColor),
                        modifier = Modifier.fillMaxWidth().clickable {
                            com.codidevs.nutriapp.data.audio.SoundManager.click()
                            if (hecha) { completadas = completadas - index; puntaje -= accion.puntos }
                            else { completadas = completadas + index; puntaje += accion.puntos }
                        }
                    ) {
                        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(text = accion.emoji, fontSize = 26.sp)
                            Spacer(Modifier.width(12.dp))
                            Text(text = accion.accion, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = if (hecha) LeafDark else Ink, modifier = Modifier.weight(1f))
                            Text(text = if (hecha) "✓" else "+${accion.puntos}", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = if (hecha) LeafDark else Mango)
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(text = "⭐ Puntaje: $puntaje", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Mango, textAlign = androidx.compose.ui.text.style.TextAlign.Center, modifier = Modifier.fillMaxWidth())

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = com.codidevs.nutriapp.data.audio.onClickConSonido { onTerminada(puntaje) },
                colors = ButtonDefaults.buttonColors(containerColor = Leaf),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .pulsoAnimado()
            ) {
                Text("Terminar", style = MaterialTheme.typography.labelLarge, color = Color.White)
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}
