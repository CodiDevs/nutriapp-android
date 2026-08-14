package com.codidevs.nutriapp.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codidevs.nutriapp.R
import com.codidevs.nutriapp.ui.theme.Leaf
import com.codidevs.nutriapp.ui.theme.LeafDark

@Composable
fun SplashScreen(onComenzar: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Fondo blanco para fundir el cuadro de la imagen
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Mostramos la imagen con recorte circular por seguridad
        Image(
            painter = painterResource(id = R.mipmap.ic_launcher_foreground),
            contentDescription = "NutriApp Logo",
            modifier = Modifier
                .size(170.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "NutriApp",
            style = MaterialTheme.typography.headlineLarge,
            color = LeafDark,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Juega con tu hijo. Alimentación y movimiento.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = com.codidevs.nutriapp.data.audio.onClickConSonido { onComenzar() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Leaf
            ),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().height(52.dp)
        ) {
            Text("Comenzar", style = MaterialTheme.typography.labelLarge)
        }
    }
}
