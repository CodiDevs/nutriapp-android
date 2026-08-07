package com.codidevs.nutriapp.ui.sendero

import androidx.compose.foundation.BorderStroke
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
import com.codidevs.nutriapp.ui.theme.Ink
import com.codidevs.nutriapp.ui.theme.InkSoft
import com.codidevs.nutriapp.ui.theme.Leaf
import com.codidevs.nutriapp.ui.theme.LeafDark
import com.codidevs.nutriapp.ui.theme.LeafLight
import com.codidevs.nutriapp.ui.theme.LineColor

data class ActividadInfo(
    val id: Int,
    val emoji: String,
    val nombre: String,
    val completada: Boolean = false
)

/** Las 7 actividades del Nivel 1 (según el maquetado y las indicaciones).
 *  Todas inician pendientes: se marcan completadas cuando el niño las juega. */
val ACTIVIDADES_NIVEL_1 = listOf(
    ActividadInfo(1, "🔍", "Descubre los alimentos"),
    ActividadInfo(2, "🥦", "¿A qué grupo pertenece?"),
    ActividadInfo(3, "✅", "Verdadero o falso"),
    ActividadInfo(4, "✏️", "Completa la frase"),
    ActividadInfo(5, "🍽️", "La mejor opción"),
    ActividadInfo(6, "🎡", "Rueda de la alimentación"),
    ActividadInfo(7, "🧠", "Memoria nutritiva")
)

@Composable
fun ActividadesScreen(
    nivelNumero: Int,
    onBack: () -> Unit,
    onActividadClick: (ActividadInfo) -> Unit
) {
    val actividades = ACTIVIDADES_NIVEL_1

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
                    onClick = { onActividadClick(actividad) }
                )
            }
        }

        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun FilaActividad(
    numero: Int,
    actividad: ActividadInfo,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(2.dp, LineColor),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ícono en círculo (verde si completada, verde suave si pendiente)
            Surface(
                color = if (actividad.completada) Leaf else LeafLight,
                shape = RoundedCornerShape(50),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = actividad.emoji, fontSize = 20.sp)
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "$numero. ${actividad.nombre}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Ink
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = if (actividad.completada) "Completada" else "Pendiente",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (actividad.completada) LeafDark else InkSoft
                )
            }

            if (actividad.completada) {
                Text(text = "✓", fontSize = 18.sp, color = LeafDark)
            }
        }
    }
}
