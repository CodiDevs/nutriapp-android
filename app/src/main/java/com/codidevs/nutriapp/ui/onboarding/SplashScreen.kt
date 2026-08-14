package com.codidevs.nutriapp.ui.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codidevs.nutriapp.ui.theme.Leaf
import com.codidevs.nutriapp.ui.theme.LeafDark

@Composable
fun SplashScreen(onComenzar: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🥦 NutriApp",
            style = MaterialTheme.typography.headlineSmall,
            color = LeafDark,
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "🦸", fontSize = 70.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Juega con tu hijo. Alimentación y movimiento.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onComenzar,
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = Leaf
            ),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().height(52.dp)
        ) {
            Text("Comenzar", style = MaterialTheme.typography.labelLarge)
        }
    }
}
