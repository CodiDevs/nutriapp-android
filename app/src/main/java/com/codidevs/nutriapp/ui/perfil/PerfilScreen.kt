package com.codidevs.nutriapp.ui.perfil

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
import com.codidevs.nutriapp.ui.theme.Berry
import com.codidevs.nutriapp.ui.theme.Ink
import com.codidevs.nutriapp.ui.theme.InkSoft
import com.codidevs.nutriapp.ui.theme.LeafDark
import com.codidevs.nutriapp.ui.theme.LeafLight
import com.codidevs.nutriapp.ui.theme.LineColor
import com.codidevs.nutriapp.ui.theme.Mango
import com.codidevs.nutriapp.ui.theme.MangoDark
import com.codidevs.nutriapp.ui.theme.Sky
import com.codidevs.nutriapp.data.models.MedallaInfo

/**
 * Pantalla de Perfil: muestra las stats, la medalla de perfil (la más reciente
 * desbloqueada o la elegida), y las medallas desbloqueadas con opción de poner en el perfil.
 */
@Composable
fun PerfilScreen(
    nombre: String,
    nivel: Int,
    medallas: List<MedallaInfo>,
    medallaPerfil: String, // id de la medalla puesta en el perfil ("" = ninguna)
    onPonerMedalla: (String) -> Unit,
    onVerRecompensas: () -> Unit,
    onCrearRegistro: () -> Unit
) {
    // La medalla que se muestra en la cabecera: la elegida, o la más reciente desbloqueada
    val medallaMostrada = medallas.firstOrNull { it.id == medallaPerfil }
        ?: medallas.filter { it.desbloqueada }.maxByOrNull { it.orden }
    val desbloqueadas = medallas.filter { it.desbloqueada }
    // Mostrar opción de "poner en perfil" para la medalla tocada
    var medallaSeleccionada by remember { mutableStateOf<String?>(null) }
    // Diálogo de confirmación para crear otro registro
    var mostrarDialogoRegistro by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Spacer(Modifier.height(4.dp))

        // Cabecera del perfil
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = Mango,
                shape = CircleShape,
                border = BorderStroke(3.dp, MangoDark),
                modifier = Modifier.size(66.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "🦸", fontSize = 34.sp)
                }
            }
            Spacer(Modifier.width(14.dp))
            Column {
                Text(
                    text = nombre,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Ink
                )
                // Nivel + medalla de perfil (la figura sin tarjeta, al lado del texto)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Nivel $nivel · ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = InkSoft,
                        fontWeight = FontWeight.Bold
                    )
                    if (medallaMostrada != null) {
                        Text(
                            text = medallaMostrada.emoji,
                            fontSize = 22.sp
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = medallaMostrada.nombre,
                            style = MaterialTheme.typography.bodyMedium,
                            color = InkSoft,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            text = "Explorador de alimentos",
                            style = MaterialTheme.typography.bodyMedium,
                            color = InkSoft,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Mis medallas (tarjetas estilo minijuegos, más grandes)
        Text(
            text = "Mis medallas",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Ink
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "Toca una medalla para ponerla en tu perfil",
            style = MaterialTheme.typography.bodySmall,
            color = InkSoft,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        // Grilla 2x2 de medallas desbloqueadas (todas, incluida la especial)
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            desbloqueadas.chunked(2).forEach { fila ->
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    fila.forEach { medalla ->
                        MedalCell(
                            medalla = medalla,
                            esPerfil = medalla.id == medallaPerfil,
                            modifier = Modifier.weight(1f),
                            onClick = { medallaSeleccionada = medalla.id }
                        )
                    }
                    if (fila.size == 1) Spacer(Modifier.weight(1f))
                }
            }
        }

        // Opción de poner en perfil (cuando se toca una medalla)
        medallaSeleccionada?.let { id ->
            val medalla = medallas.firstOrNull { it.id == id }
            Spacer(Modifier.height(12.dp))
            Surface(
                color = LeafLight,
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(2.dp, LeafDark.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${medalla?.emoji ?: ""} ${medalla?.nombre ?: ""}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = LeafDark
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = {
                            onPonerMedalla(id)
                            medallaSeleccionada = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = LeafDark),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth().height(44.dp)
                    ) {
                        Text(
                            text = "Poner en el perfil",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Ver recompensas
        Button(
            onClick = onVerRecompensas,
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(2.dp, LineColor),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text(
                text = "Ver recompensas",
                style = MaterialTheme.typography.labelLarge,
                color = LeafDark
            )
        }

        Spacer(Modifier.height(12.dp))

        // Crear otro registro
        OutlinedButton(
            onClick = { mostrarDialogoRegistro = true },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Berry),
            border = BorderStroke(2.dp, Berry),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text(
                text = "Crear otro registro",
                style = MaterialTheme.typography.labelLarge,
                color = Berry
            )
        }

        Spacer(Modifier.height(20.dp))
    }

    // Aviso de confirmación antes de crear otro registro
    if (mostrarDialogoRegistro) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoRegistro = false },
            title = { Text("¿Crear otro registro?", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "Ten en cuenta que el registro actual con todo su progreso " +
                        "(niveles, monedas y medallas) se eliminará."
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    mostrarDialogoRegistro = false
                    onCrearRegistro()
                }) {
                    Text("Sí, crear", color = Berry, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoRegistro = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun MedalCell(
    medalla: MedallaInfo,
    esPerfil: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        color = if (esPerfil) LeafLight else Color.White,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(if (esPerfil) 3.dp else 2.dp, if (esPerfil) LeafDark else LineColor),
        shadowElevation = 3.dp,
        modifier = modifier
            .height(110.dp)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = medalla.emoji, fontSize = 34.sp)
            Spacer(Modifier.height(6.dp))
            Text(
                text = medalla.nombre,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Ink,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 6.dp)
            )
            if (esPerfil) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "✓ En perfil",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = LeafDark
                )
            }
        }
    }
}
