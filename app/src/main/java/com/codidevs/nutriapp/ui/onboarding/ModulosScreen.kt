package com.codidevs.nutriapp.ui.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codidevs.nutriapp.ui.components.ScreenHeader
import com.codidevs.nutriapp.ui.theme.Leaf
import com.codidevs.nutriapp.ui.theme.LeafDark
import com.codidevs.nutriapp.ui.theme.LeafLight
import com.codidevs.nutriapp.ui.theme.Mango
import com.codidevs.nutriapp.ui.theme.MangoLight
import com.codidevs.nutriapp.ui.theme.InkSoft
import com.codidevs.nutriapp.ui.theme.Locked
import com.codidevs.nutriapp.ui.theme.Sky
import com.codidevs.nutriapp.ui.theme.BgApp

@Composable
fun ModulosScreen(
    nombre: String,
    nutricionCompletado: Boolean,
    actividadCompletado: Boolean,
    nivelNutricion: Int, // nivel actual en el módulo de nutrición (1-3)
    onBack: () -> Unit,
    onNutricion: () -> Unit,
    onActividadFisica: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        ScreenHeader(titulo = "Elige un módulo", onBack = onBack)

        Spacer(Modifier.height(12.dp))

        Text(
            text = "¡Hola $nombre! Escoge con qué quieres aprender hoy",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Módulo de Nutrición (disponible)
            Card(
                onClick = onNutricion,
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(2.dp, Leaf),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth().height(96.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Emoji en cuadro de tamaño fijo para que los tres queden centrados igual
                    Box(
                        modifier = Modifier.size(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🌱", fontSize = 40.sp)
                    }
                    Spacer(Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Nutrición",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = LeafDark
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = if (nutricionCompletado)
                                "Completado ✓"
                            else
                                "Nivel $nivelNutricion de 3",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (nutricionCompletado) LeafDark else InkSoft,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Box(
                        modifier = Modifier.size(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "▶", fontSize = 20.sp, color = LeafDark)
                    }
                }
            }

            // Módulo de Actividad física (bloqueado hasta completar Nutrición)
            val actividadBloqueada = !nutricionCompletado
            Card(
                onClick = { if (!actividadBloqueada) onActividadFisica() },
                enabled = !actividadBloqueada,
                colors = CardDefaults.cardColors(
                    containerColor = if (actividadBloqueada) Locked.copy(alpha = 0.2f) else Color.White
                ),
                border = BorderStroke(
                    2.dp,
                    if (actividadBloqueada) Locked else Sky
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth().height(96.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Emoji en cuadro de tamaño fijo para que los tres queden centrados igual
                    Box(
                        modifier = Modifier.size(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🏃",
                            fontSize = 40.sp,
                            modifier = Modifier.alpha(if (actividadBloqueada) 0.5f else 1f)
                        )
                    }
                    Spacer(Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Actividad física",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = if (actividadBloqueada) InkSoft else LeafDark
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = when {
                                actividadCompletado -> "Completado ✓"
                                actividadBloqueada -> "Completa el módulo de Nutrición"
                                else -> "Niveles 4 a 7 · ¡Muévete!"
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (actividadBloqueada) InkSoft else LeafDark,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Box(
                        modifier = Modifier.size(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (actividadBloqueada) "🔒" else "▶",
                            fontSize = if (actividadBloqueada) 18.sp else 20.sp,
                            color = if (actividadBloqueada) InkSoft else LeafDark
                        )
                    }
                }
            }

            // Módulo próximo (bloqueado)
            Card(
                colors = CardDefaults.cardColors(containerColor = Locked.copy(alpha = 0.35f)),
                border = BorderStroke(2.dp, Locked.copy(alpha = 0.4f)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth().height(96.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Emoji en cuadro de tamaño fijo para que los tres queden centrados igual
                    Box(
                        modifier = Modifier.size(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🔒", fontSize = 40.sp)
                    }
                    Spacer(Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Próximamente",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = InkSoft
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Nuevo módulo en camino",
                            style = MaterialTheme.typography.bodyMedium,
                            color = InkSoft
                        )
                    }
                }
            }
        }
    }
}
