package com.codidevs.nutriapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codidevs.nutriapp.ui.theme.Cream
import com.codidevs.nutriapp.ui.theme.InkSoft
import com.codidevs.nutriapp.ui.theme.LeafDark
import com.codidevs.nutriapp.ui.theme.LineColor

/**
 * Barra de navegación inferior con las 4 pestañas principales.
 * El parámetro [tabActiva] indica cuál está seleccionada.
 */
@Composable
fun BottomNav(
    tabActiva: String,
    onTab: (String) -> Unit
) {
    Surface(
        color = Cream,
        border = BorderStroke(2.dp, LineColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(modifier = Modifier.weight(1f)) { NavItem("home", "🏠", "Inicio", tabActiva, onTab) }
            Box(modifier = Modifier.weight(1f)) { NavItem("sendero", "🌱", "Sendero", tabActiva, onTab) }
            Box(modifier = Modifier.weight(1f)) { NavItem("juegos", "🎮", "Juegos", tabActiva, onTab) }
            Box(modifier = Modifier.weight(1f)) { NavItem("perfil", "👤", "Perfil", tabActiva, onTab) }
        }
    }
}

@Composable
private fun NavItem(
    id: String,
    emoji: String,
    etiqueta: String,
    tabActiva: String,
    onTab: (String) -> Unit
) {
    val activa = tabActiva == id
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = com.codidevs.nutriapp.data.audio.onClickConSonido { onTab(id) })
            .padding(vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = emoji, fontSize = 22.sp)
        Spacer(Modifier.height(2.dp))
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = if (activa) LeafDark else InkSoft
        )
    }
}
