package com.codidevs.nutriapp.ui.sendero

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import com.codidevs.nutriapp.ui.theme.BgApp
import com.codidevs.nutriapp.ui.theme.Ink
import com.codidevs.nutriapp.ui.theme.InkSoft
import com.codidevs.nutriapp.ui.theme.Leaf
import com.codidevs.nutriapp.ui.theme.LeafDark
import com.codidevs.nutriapp.ui.theme.LineColor
import com.codidevs.nutriapp.ui.theme.Mango

data class NivelInfo(
    val numero: Int,
    val emoji: String,
    val titulo: String,
    val descripcion: String,
    val actividades: Int,
    val monedas: String,
    val puntosMaximos: Int = 0
)

val NIVELES_INFO = listOf(
    NivelInfo(1, "🍎", "Conozco los alimentos", "Aprende a reconocer los grupos de alimentos", 7, "+20"),
    NivelInfo(2, "🥦", "Descubro los nutrientes", "Descubre qué nutrientes te dan las comidas", 5, "+20"),
    NivelInfo(3, "🍽️", "Armo mi plato saludable", "Combina alimentos para un plato sano", 4, "+25"),
    NivelInfo(4, "🏃", "Campeón de actividad física", "¡Muévete y fortalece tu cuerpo!", 6, "+30"),
    NivelInfo(5, "💧", "Cuido mi cuerpo y mi mente", "Hidratación, descanso y bienestar", 4, "+20"),
    NivelInfo(6, "🦷", "Elijo hábitos saludables", "Higiene y hábitos para todos los días", 5, "+25"),
    NivelInfo(7, "🦸", "Superhéroe de la salud", "¡Conviértete en el héroe de tu salud!", 3, "+50")
)

@Composable
fun NivelDetalleScreen(
    nivel: NivelInfo,
    onBack: () -> Unit,
    onVerActividades: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgApp)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        ScreenHeader(titulo = "Detalle del nivel", onBack = onBack)

        Spacer(Modifier.height(24.dp))

        // Héroe del nivel
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = nivel.emoji, fontSize = 56.sp)
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Nivel ${nivel.numero} · ${nivel.titulo}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Ink,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = nivel.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = InkSoft,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.height(20.dp))

        // Stats: Actividades, Monedas, Estrellas, Puntos
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatBox("${nivel.actividades}", "Actividades", Modifier.weight(1f))
                StatBox(nivel.monedas, "Monedas", Modifier.weight(1f))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatBox("⭐⭐⭐", "Estrellas", Modifier.weight(1f))
                StatBox("${nivel.puntosMaximos}", "Puntos", Modifier.weight(1f))
            }
        }

        Spacer(Modifier.height(28.dp))

        // Ver actividades
        Button(
            onClick = com.codidevs.nutriapp.data.audio.onClickConSonido { onVerActividades() },
            colors = ButtonDefaults.buttonColors(containerColor = Leaf),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().height(52.dp)
        ) {
            Text("Ver actividades", style = MaterialTheme.typography.labelLarge, color = Color.White)
        }
    }
}

@Composable
private fun StatBox(valor: String, etiqueta: String, modifier: Modifier = Modifier) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(2.dp, LineColor),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = valor,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = LeafDark
            )
            Text(
                text = etiqueta,
                style = MaterialTheme.typography.labelSmall,
                color = InkSoft,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
