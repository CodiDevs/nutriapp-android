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
import kotlinx.coroutines.delay
import com.codidevs.nutriapp.ui.components.NotaMensaje
import com.codidevs.nutriapp.ui.theme.Ink
import com.codidevs.nutriapp.ui.theme.InkSoft
import com.codidevs.nutriapp.ui.theme.Leaf
import com.codidevs.nutriapp.ui.theme.LeafDark
import com.codidevs.nutriapp.ui.theme.LeafLight
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
    modulo: Int, // 1 = Nutrición (niveles 1-3), 2 = Actividad física (niveles 4-7)
    nivelesDesbloqueados: Int, // cuántos niveles están desbloqueados (1+)
    estrellasNivel: Map<Int, Int>, // nivel -> estrellas totales (0-3)
    onNivelClick: (Int) -> Unit,
    onElegirModulo: () -> Unit,
    onCambiarModulo: () -> Unit
) {
    var mostrarBloqueado by remember { mutableStateOf(false) }

    // Niveles según el módulo
    val rango = if (modulo == 1) 1..3 else 4..7
    val titulo = if (modulo == 1) "Sendero de Nutrición" else "Sendero de Actividad Física"
    val emojiMascota = if (modulo == 1) "🍎" else "🏃"
    val mensajeMascota = if (modulo == 1)
        "¡Sigamos aprendiendo sobre los alimentos!"
    else
        "¡Muévete, juega y cuida tu cuerpo!"

    // Niveles del módulo: el primero siempre disponible; los siguientes según el progreso
    val niveles = remember(modulo, nivelesDesbloqueados) {
        NIVELES.filter { it.numero in rango }.map { nivel ->
            // Para el módulo 2, el nivel 4 se considera desbloqueado al entrar al módulo
            val desbloqueado = if (modulo == 2) {
                nivel.numero == rango.first || nivel.numero <= nivelesDesbloqueados
            } else {
                nivel.numero <= nivelesDesbloqueados
            }
            nivel.copy(
                bloqueado = !desbloqueado,
                actual = if (modulo == 2) nivel.numero == nivelesDesbloqueados.coerceIn(rango) else nivel.numero == nivelesDesbloqueados
            )
        }
    }

    // El módulo está completo si el último nivel del rango está desbloqueado
    val moduloCompleto = nivelesDesbloqueados >= rango.last

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Spacer(Modifier.height(4.dp))

            Text(
                text = titulo,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Ink
            )

            Spacer(Modifier.height(14.dp))

            // Nota de la mascota (estilo nota adhesiva, no botón)
            NotaMensaje(
                emoji = emojiMascota,
                titulo = mensajeMascota,
                colorFondo = Sky.copy(alpha = 0.12f),
                colorTexto = Ink
            )

            Spacer(Modifier.height(20.dp))

            // Nodos del sendero en zigzag
            niveles.forEach { nivel ->
                NodoNivel(
                    nivel = nivel,
                    estrellas = estrellasNivel[nivel.numero] ?: 0,
                    onClick = {
                        if (nivel.bloqueado) {
                            mostrarBloqueado = true
                        } else {
                            onNivelClick(nivel.numero)
                        }
                    },
                    reversed = nivel.numero % 2 == 0
                )
            }

            Spacer(Modifier.height(24.dp))

            // Botón para elegir el siguiente módulo (cuando este módulo está completo)
            if (moduloCompleto && modulo == 1) {
                Button(
                    onClick = com.codidevs.nutriapp.data.audio.onClickConSonido { onElegirModulo() },
                    colors = ButtonDefaults.buttonColors(containerColor = Mango),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp)
                ) {
                    Text(
                        text = "Elegir el siguiente módulo →",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White
                    )
                }
            }

            // Botón para cambiar de módulo (visible en el módulo 2, y en el 1 si está completo)
            if (modulo == 2 || moduloCompleto) {
                Spacer(Modifier.height(10.dp))
                OutlinedButton(
                    onClick = com.codidevs.nutriapp.data.audio.onClickConSonido { onCambiarModulo() },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp)
                ) {
                    Text(
                        text = if (modulo == 1) "Ver módulos" else "Cambiar a Nutrición 🌱",
                        style = MaterialTheme.typography.labelLarge,
                        color = LeafDark
                    )
                }
            }

            Spacer(Modifier.height(80.dp)) // Espacio extra al final para que el Snackbar no tape el último botón
        }

        // Aviso de nivel bloqueado (estilo flotante)
        if (mostrarBloqueado) {
            LaunchedEffect(mostrarBloqueado) {
                delay(2200)
                mostrarBloqueado = false
            }
            // Snackbar Host para manejar el snackbar sin afectar el layout
            SnackbarHost(
                hostState = remember { SnackbarHostState() },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            ) {
                Snackbar(
                    containerColor = Ink,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("🔒 Completa el nivel anterior para desbloquearlo")
                }
            }
        }
    }
}

@Composable
private fun NodoNivel(
    nivel: NivelSendero,
    estrellas: Int,
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
            Nodo(nivel = nivel, estrellas = estrellas, onClick = onClick)
            Spacer(Modifier.width(16.dp))
            Etiqueta(nivel = nivel, modifier = Modifier.weight(1f))
        } else {
            Etiqueta(nivel = nivel, modifier = Modifier.weight(1f))
            Spacer(Modifier.width(16.dp))
            Nodo(nivel = nivel, estrellas = estrellas, onClick = onClick)
        }
    }
}

@Composable
private fun Nodo(
    nivel: NivelSendero,
    estrellas: Int, // estrellas totales del nivel (0-3)
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .shadow(4.dp, CircleShape)
                .clickable(onClick = com.codidevs.nutriapp.data.audio.onClickConSonido { onClick() })
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
        // Estrellas del nivel debajo del nodo
        if (estrellas > 0) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = when (estrellas) {
                    3 -> "⭐⭐⭐"
                    2 -> "⭐⭐"
                    else -> "⭐"
                },
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun Etiqueta(nivel: NivelSendero, modifier: Modifier = Modifier) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(2.dp, if (nivel.bloqueado) LineColor else Leaf.copy(alpha = 0.4f)),
        shadowElevation = 3.dp,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Círculo con el número del nivel
            Surface(
                color = if (nivel.bloqueado) Locked.copy(alpha = 0.4f) else LeafLight,
                shape = CircleShape,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "${nivel.numero}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (nivel.bloqueado) InkSoft else LeafDark
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = nivel.titulo,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (nivel.bloqueado) InkSoft.copy(alpha = 0.7f) else LeafDark
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
}
