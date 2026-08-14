package com.codidevs.nutriapp.ui.home

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.codidevs.nutriapp.ui.components.NotaMensaje
import com.codidevs.nutriapp.ui.theme.Ink
import com.codidevs.nutriapp.ui.theme.InkSoft
import com.codidevs.nutriapp.ui.theme.LeafDark
import com.codidevs.nutriapp.ui.theme.LeafLight
import com.codidevs.nutriapp.ui.theme.LineColor
import com.codidevs.nutriapp.ui.theme.Mango
import com.codidevs.nutriapp.ui.theme.MangoDark
import com.codidevs.nutriapp.ui.theme.MangoLight

@Composable
fun HomeScreen(
    nombre: String,
    sexo: String = "niño",
    nivelTexto: String,
    onSendero: () -> Unit,
    onRecompensas: () -> Unit,
    onPerfil: () -> Unit
) {
    // Evita clics repetidos que bugean la navegación
    var yaHaciendoClick by remember { mutableStateOf(false) }

    // Saludo dinámico según la hora
    val saludo = remember {
        val hora = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        when (hora) {
            in 6..11 -> "¡Buenos días!"
            in 12..18 -> "¡Buenas tardes!"
            else -> "¡Buenas noches!"
        }
    }

    // Seguridad: desbloquea siempre tras un breve tiempo para evitar quedar bloqueado
    LaunchedEffect(yaHaciendoClick) {
        if (yaHaciendoClick) {
            delay(1000)
            yaHaciendoClick = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Spacer(Modifier.height(8.dp))

        // Saludo dinámico y nivel/módulo reales
        Text(
            text = "$saludo, $nombre",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Ink
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = nivelTexto,
            style = MaterialTheme.typography.bodyLarge,
            color = InkSoft,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(24.dp))

        // Misión del día
        Card(
            colors = CardDefaults.cardColors(containerColor = MangoLight),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🎯  Misión del día",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MangoDark
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Completa una actividad para ganar monedas.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = InkSoft,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = com.codidevs.nutriapp.data.audio.onClickConSonido {
                        if (!yaHaciendoClick) {
                            yaHaciendoClick = true
                            onSendero()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Mango),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("Ir al sendero", style = MaterialTheme.typography.labelLarge, color = Color.White)
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // Accesos rápidos: Recompensas y Perfil (estilo minijuegos)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            AccessCard(
                emoji = "🎁",
                texto = "Recompensas",
                onClick = {
                    if (!yaHaciendoClick) {
                        yaHaciendoClick = true
                        onRecompensas()
                    }
                },
                modifier = Modifier.weight(1f)
            )
            AccessCard(
                emoji = "👤",
                texto = "Mi perfil",
                onClick = {
                    if (!yaHaciendoClick) {
                        yaHaciendoClick = true
                        onPerfil()
                    }
                },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(24.dp))

        // Nota motivadora (estilo nota adhesiva, no botón)
        NotaMensaje(
            emoji = if (sexo == "niña") "👧" else "👦",
            titulo = "¡Tú puedes, superhéroe de la salud!",
            subtitulo = "Sigue aprendiendo y ganando monedas",
            colorFondo = LeafLight,
            colorTexto = LeafDark
        )

        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun AccessCard(
    emoji: String,
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(22.dp),
        border = BorderStroke(2.dp, LineColor),
        shadowElevation = 3.dp,
        modifier = modifier
            .height(120.dp)
            .clickable(onClick = com.codidevs.nutriapp.data.audio.onClickConSonido { onClick() })
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Círculo con el emoji (como en los minijuegos)
            Surface(
                color = LeafLight,
                shape = CircleShape,
                modifier = Modifier.size(52.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = emoji, fontSize = 26.sp)
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = texto,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = LeafDark,
                textAlign = TextAlign.Center
            )
        }
    }
}
