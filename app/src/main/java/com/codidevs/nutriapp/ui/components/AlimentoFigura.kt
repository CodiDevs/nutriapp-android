package com.codidevs.nutriapp.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Muestra la figura de un alimento: usa un icono dibujado (Canvas) cuando el
 * alimento no tiene un emoji claro, o el emoji en caso contrario.
 */
@Composable
fun AlimentoFigura(
    nombre: String,
    emoji: String,
    tamano: Int = 40,
    modifier: Modifier = Modifier
) {
    when (nombre) {
        "Garbanzos" -> GarbanzoIcon(modifier = modifier.size(tamano.dp))
        "Papaya" -> PapayaIcon(modifier = modifier.size(tamano.dp))
        "Lentejas" -> LentejasIcon(modifier = modifier.size(tamano.dp))
        "Espinaca" -> EspinacaIcon(modifier = modifier.size(tamano.dp))
        "Yogur" -> YogurIcon(modifier = modifier.size(tamano.dp))
        "Tomate" -> TomatesIcon(modifier = modifier.size(tamano.dp))
        else -> Text(text = emoji, fontSize = tamano.sp)
    }
}
