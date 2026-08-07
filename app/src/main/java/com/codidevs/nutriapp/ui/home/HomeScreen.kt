package com.codidevs.nutriapp.ui.home

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codidevs.nutriapp.ui.theme.Ink
import com.codidevs.nutriapp.ui.theme.InkSoft
import com.codidevs.nutriapp.ui.theme.Mango
import com.codidevs.nutriapp.ui.theme.MangoDark
import com.codidevs.nutriapp.ui.theme.MangoLight

@Composable
fun HomeScreen(
    nombre: String,
    onSendero: () -> Unit,
    onRecompensas: () -> Unit,
    onPerfil: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Spacer(Modifier.height(8.dp))

        // Saludo y nivel
        Text(
            text = "Buenos días, $nombre",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Ink
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Nivel 1 · Nutrición",
            style = MaterialTheme.typography.bodyLarge,
            color = InkSoft
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
                    onClick = onSendero,
                    colors = ButtonDefaults.buttonColors(containerColor = Mango),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("Ir al sendero", style = MaterialTheme.typography.labelLarge, color = Color.White)
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // Accesos rápidos: Recompensas y Perfil
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AccessCard(
                emoji = "🎁",
                texto = "Recompensas",
                onClick = onRecompensas,
                modifier = Modifier.weight(1f)
            )
            AccessCard(
                emoji = "👤",
                texto = "Mi perfil",
                onClick = onPerfil,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun AccessCard(
    emoji: String,
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(2.dp, Color(0xFFE7E0CC)),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier.height(120.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = emoji, fontSize = 34.sp)
            Spacer(Modifier.height(8.dp))
            Text(
                text = texto,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Ink,
                textAlign = TextAlign.Center
            )
        }
    }
}
