package com.codidevs.nutriapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Estructura base para las 4 pantallas principales con barra de navegación inferior:
 * Inicio, Sendero, Juegos y Perfil. Coloca la StatsBar arriba, el contenido al
 * centro y la BottomNav abajo.
 */
@Composable
fun TabScaffold(
    tabActiva: String,
    onTab: (String) -> Unit,
    content: @Composable (Modifier) -> Unit
) {
    Scaffold(
        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.background,
        bottomBar = {
            BottomNav(tabActiva = tabActiva, onTab = onTab)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            StatsBar(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
            )
            content(Modifier.weight(1f))
        }
    }
}
