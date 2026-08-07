package com.codidevs.nutriapp.ui.sendero

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codidevs.nutriapp.ui.theme.Ink
import com.codidevs.nutriapp.ui.theme.InkSoft
import com.codidevs.nutriapp.ui.theme.Leaf
import com.codidevs.nutriapp.ui.theme.LeafDark
import com.codidevs.nutriapp.ui.theme.LineColor
import com.codidevs.nutriapp.ui.theme.Locked
import com.codidevs.nutriapp.ui.theme.Mango
import com.codidevs.nutriapp.ui.theme.Sky

private data class NivelSendero(
    val numero: Int,
    val emoji: String,
    val titulo: String,
    val descripcion: String,
    val bloqueado: Boolean,
    val actual: Boolean = false
)

private val NIVELES = listOf(
    NivelSendero(1, "🍎", "Conozco los alimentos", "¡Empecemos!", bloqueado = false, actual = true),
    NivelSendero(2, "🥦", "Descubro los nutrientes", "Aprende qué te da cada comida", bloqueado = true),
    NivelSendero(3, "🍽️", "Armo mi plato saludable", "Combina alimentos sanos", bloqueado = true),
    NivelSendero(4, "🏃", "Campeón de actividad física", "¡Muévete!", bloqueado = true),
    NivelSendero(5, "💧", "Cuido mi cuerpo y mi mente", "Hidratación y descanso", bloqueado = true),
    NivelSendero(6, "🦷", "Elijo hábitos saludables", "Higiene y buenos hábitos", bloqueado = true),
    NivelSendero(7, "🦸", "Superhéroe de la salud", "¡Conviértete en un héroe!", bloqueado = true)
)

@Composable
fun SenderoScreen(
    onNivelClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Spacer(Modifier.height(4.dp))

        Text(
            text = "Sendero de Nutrición",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Ink
        )

        Spacer(Modifier.height(14.dp))

        // Globo de la mascota
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(2.dp, Sky),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "🍎", fontSize = 32.sp)
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "¡Sigamos aprendiendo sobre los alimentos!",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Ink
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // Nodos del sendero en zigzag
        NIVELES.forEach { nivel ->
            NodoNivel(
                nivel = nivel,
                onClick = { onNivelClick(nivel.numero) },
                reversed = nivel.numero % 2 == 0
            )
        }

        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun NodoNivel(
    nivel: NivelSendero,
    onClick: () -> Unit,
    reversed: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = if (reversed) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!reversed) {
            Nodo(nivel = nivel, onClick = onClick)
            Spacer(Modifier.width(16.dp))
            Etiqueta(nivel = nivel, modifier = Modifier.weight(1f))
        } else {
            Etiqueta(nivel = nivel, modifier = Modifier.weight(1f))
            Spacer(Modifier.width(16.dp))
            Nodo(nivel = nivel, onClick = onClick)
        }
    }
}

@Composable
private fun Nodo(nivel: NivelSendero, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .shadow(4.dp, CircleShape)
            .then(
                if (nivel.bloqueado) {
                    Modifier
                } else {
                    Modifier.clickable(onClick = onClick)
                }
            )
            .background(
                color = if (nivel.bloqueado) Locked else Leaf,
                shape = CircleShape
            )
            .border(3.dp, if (nivel.bloqueado) Color(0xFFB9B39E) else LeafDark, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        // Borde punteado si es el nivel actual
        if (nivel.actual) {
            Box(
                modifier = Modifier
                    .size(78.dp)
                    .border(3.dp, Mango, CircleShape)
            )
        }
        Text(text = nivel.emoji, fontSize = 28.sp)
    }
}

@Composable
private fun Etiqueta(nivel: NivelSendero, modifier: Modifier = Modifier) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(2.dp, LineColor),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
            Text(
                text = "Nivel ${nivel.numero}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Ink
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = nivel.titulo,
                style = MaterialTheme.typography.bodyMedium,
                color = if (nivel.bloqueado) InkSoft.copy(alpha = 0.7f) else LeafDark,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = nivel.descripcion,
                style = MaterialTheme.typography.bodySmall,
                color = InkSoft
            )
        }
    }
}
