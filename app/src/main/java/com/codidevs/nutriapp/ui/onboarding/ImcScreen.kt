package com.codidevs.nutriapp.ui.onboarding

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
import com.codidevs.nutriapp.ui.theme.Leaf
import com.codidevs.nutriapp.ui.theme.LeafDark
import com.codidevs.nutriapp.ui.theme.LeafLight
import com.codidevs.nutriapp.ui.theme.Mango
import com.codidevs.nutriapp.ui.theme.MangoLight
import com.codidevs.nutriapp.ui.theme.Sky
import com.codidevs.nutriapp.ui.theme.Berry
import com.codidevs.nutriapp.ui.theme.BerryLight
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * Cálculo del IMC y su clasificación para niños de 6 a 12 años.
 * El IMC se calcula igual que en adultos, pero se clasifica según percentiles
 * por edad (referencia OMS pediátrica, simplificada).
 */
fun calcularImc(pesoKg: Double, estaturaCm: Double): Double {
    if (pesoKg <= 0 || estaturaCm <= 0) return 0.0
    val alturaM = estaturaCm / 100.0
    return pesoKg / (alturaM * alturaM)
}

enum class ClasificacionImc(
    val etiqueta: String,
    val emoji: String,
    val color: Color,
    val colorFondo: Color,
    val mensaje: String
) {
    BAJO_PESO(
        "Bajo peso",
        "🍽️",
        Mango,
        MangoLight,
        "Comamos más y mejor juntos"
    ),
    NORMAL(
        "Peso normal",
        "🍎",
        Leaf,
        LeafLight,
        "¡Muy bien! Ahora aprenderemos juntos hábitos saludables."
    ),
    SOBREPESO(
        "Sobrepeso",
        "⚖️",
        Sky,
        Color(0xFFE3F4FA),
        "Movamos el cuerpo y comamos más sano"
    ),
    OBESIDAD(
        "Obesidad",
        "🏃",
        Berry,
        BerryLight,
        "¡Vamos a cuidarte con actividad y buena comida!"
    )
}

/** Clasificación simplificada por edad (años) según la referencia OMS pediátrica. */
fun clasificarImc(imc: Double, edadAnios: Int): ClasificacionImc {
    // Rangos de IMC "normales" aproximados por edad (OMS)
    val normalMin = when (edadAnios) {
        6 -> 13.5; 7 -> 13.5; 8 -> 13.6; 9 -> 13.8; 10 -> 14.0
        11 -> 14.2; 12 -> 14.5
        else -> 14.0
    }
    val sobrepesoMin = when (edadAnios) {
        6 -> 17.5; 7 -> 18.0; 8 -> 18.6; 9 -> 19.2; 10 -> 19.8
        11 -> 20.5; 12 -> 21.2
        else -> 20.0
    }
    val obesidadMin = sobrepesoMin + 3.0

    return when {
        imc < normalMin -> ClasificacionImc.BAJO_PESO
        imc < sobrepesoMin -> ClasificacionImc.NORMAL
        imc < obesidadMin -> ClasificacionImc.SOBREPESO
        else -> ClasificacionImc.OBESIDAD
    }
}

@Composable
fun ImcScreen(
    nombre: String,
    edad: Int,
    peso: Double,
    estatura: Double,
    onBack: () -> Unit,
    onAventura: () -> Unit
) {
    val imc = remember(peso, estatura) { calcularImc(peso, estatura) }
    val clasificacion = remember(imc, edad) { clasificarImc(imc, edad) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(12.dp))

        ScreenHeader(titulo = "Tu estado nutricional", onBack = onBack)

        Spacer(Modifier.height(28.dp))

        // Número grande del IMC
        Text(
            text = String.format("%.1f", imc),
            fontSize = 72.sp,
            fontWeight = FontWeight.ExtraBold,
            color = clasificacion.color
        )

        Text(
            text = "IMC",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(20.dp))

        // Tarjeta con la clasificación
        Card(
            colors = CardDefaults.cardColors(containerColor = clasificacion.colorFondo),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${clasificacion.emoji}  ${clasificacion.etiqueta}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = clasificacion.color
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = clasificacion.mensaje,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // Botón de aventura
        Button(
            onClick = com.codidevs.nutriapp.data.audio.onClickConSonido { onAventura() },
            colors = ButtonDefaults.buttonColors(containerColor = Leaf),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().height(52.dp)
        ) {
            Text("Comenzar aventura", style = MaterialTheme.typography.labelLarge, color = Color.White)
        }

        Spacer(Modifier.height(16.dp))

        // Información del niño
        Text(
            text = "$nombre · $edad años · ${peso} kg · ${estatura} cm",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
