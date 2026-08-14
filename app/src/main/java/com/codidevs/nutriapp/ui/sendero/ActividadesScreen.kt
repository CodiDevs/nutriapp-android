package com.codidevs.nutriapp.ui.sendero

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codidevs.nutriapp.ui.components.ScreenHeader
import com.codidevs.nutriapp.ui.theme.*

data class ActividadInfo(
    val id: Int,
    val emoji: String,
    val nombre: String,
    val completada: Boolean = false,
    val porcentaje: Int = -1 // -1 si no jugada
)

@Composable
fun ActividadesScreen(
    nivelNumero: Int,
    actividades: List<ActividadInfo>,
    estrellas: Map<Int, Int>, // actividadId -> estrellas (0-3, -1 si no jugada)
    porcentajes: Map<Int, Int>, // actividadId -> porcentaje (0-100, -1 si no jugada)
    onBack: () -> Unit,
    onActividadClick: (ActividadInfo) -> Unit,
    onNivelCompletado: () -> Unit
) {
    val nivelCompleto = actividades.all { (estrellas[it.id] ?: 0) > 0 }

    Box(modifier = Modifier.fillMaxSize().background(BgApp)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            ScreenHeader(titulo = "Actividades · Nivel $nivelNumero", onBack = onBack)

            Spacer(Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                actividades.forEachIndexed { index, actividad ->
                    FilaActividad(
                        numero = index + 1,
                        actividad = actividad,
                        estrellas = estrellas[actividad.id] ?: -1,
                        porcentaje = porcentajes[actividad.id] ?: -1,
                        onClick = { onActividadClick(actividad) }
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            if (nivelCompleto) {
                Button(
                    onClick = com.codidevs.nutriapp.data.audio.onClickConSonido { onNivelCompletado() },
                    colors = ButtonDefaults.buttonColors(containerColor = Leaf),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp)
                ) {
                    Text(
                        text = if (nivelNumero >= 7) "Finalizar" else "Siguiente →",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White
                    )
                }
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
private fun FilaActividad(
    numero: Int,
    actividad: ActividadInfo,
    estrellas: Int,
    porcentaje: Int,
    onClick: () -> Unit
) {
    val jugada = estrellas >= 0
    val completada = estrellas > 0
    Card(
        onClick = com.codidevs.nutriapp.data.audio.onClickConSonido { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(2.dp, if (completada) Leaf else LineColor),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = if (completada) Leaf else LeafLight,
                shape = RoundedCornerShape(50),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = actividad.emoji, fontSize = 20.sp)
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = "$numero. ${actividad.nombre}", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Ink)
                Spacer(Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = when {
                            !jugada -> "Pendiente"
                            estrellas == 0 -> "Intentada"
                            else -> "Completada"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (completada) LeafDark else InkSoft
                    )
                    if (jugada) {
                        Text(
                            text = " · $porcentaje%",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (porcentaje >= 100) LeafDark else Mango
                        )
                    }
                }
            }

            if (jugada) {
                Text(
                    text = when (estrellas) {
                        3 -> "⭐⭐⭐"
                        2 -> "⭐⭐"
                        1 -> "⭐"
                        else -> "☆"
                    },
                    fontSize = 14.sp
                )
            }
        }
    }
}
