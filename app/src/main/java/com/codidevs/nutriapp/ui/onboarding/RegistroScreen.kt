package com.codidevs.nutriapp.ui.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codidevs.nutriapp.ui.components.ScreenHeader
import com.codidevs.nutriapp.ui.theme.Leaf
import com.codidevs.nutriapp.ui.theme.LeafDark
import com.codidevs.nutriapp.ui.theme.LeafLight
import com.codidevs.nutriapp.ui.theme.Mango
import com.codidevs.nutriapp.ui.theme.MangoLight

/**
 * Pantalla de Registro: datos generales del niño (nombre, edad, peso, estatura).
 * El usuario presiona "Continuar" y pasa al cálculo del IMC.
 */
@Composable
fun RegistroScreen(
    onBack: () -> Unit,
    onContinuar: (nombre: String, edad: String, peso: String, estatura: String) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var estatura by remember { mutableStateOf("") }

    var errorNombre by remember { mutableStateOf<String?>(null) }
    var errorEdad by remember { mutableStateOf<String?>(null) }
    var errorPeso by remember { mutableStateOf<String?>(null) }
    var errorEstatura by remember { mutableStateOf<String?>(null) }

    fun validarYContinuar() {
        errorNombre = if (nombre.isBlank()) "Escribe tu nombre" else null
        errorEdad = when {
            edad.isBlank() -> "Escribe tu edad"
            edad.toIntOrNull() == null -> "Solo números"
            edad.toInt() !in 4..14 -> "Debe ser entre 4 y 14 años"
            else -> null
        }
        errorPeso = when {
            peso.isBlank() -> "Escribe tu peso"
            peso.toDoubleOrNull() == null -> "Solo números"
            peso.toDouble() <= 0 -> "Peso no válido"
            else -> null
        }
        errorEstatura = when {
            estatura.isBlank() -> "Escribe tu estatura"
            estatura.toDoubleOrNull() == null -> "Solo números"
            estatura.toDouble() <= 0 -> "Estatura no válida"
            else -> null
        }
        val ok = errorNombre == null && errorEdad == null && errorPeso == null && errorEstatura == null
        if (ok) {
            onContinuar(nombre.trim(), edad.trim(), peso.trim(), estatura.trim())
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(12.dp))

        ScreenHeader(titulo = "Cuéntanos de ti", onBack = onBack)

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Así sabremos cómo ayudarte a crecer sano",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        // Nombre
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            placeholder = { Text("Ej. Sofía") },
            singleLine = true,
            isError = errorNombre != null,
            supportingText = { errorNombre?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Leaf,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        // Edad
        OutlinedTextField(
            value = edad,
            onValueChange = { if (it.length <= 2) edad = it.filter(Char::isDigit) },
            label = { Text("Edad (años)") },
            singleLine = true,
            isError = errorEdad != null,
            supportingText = { errorEdad?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Leaf,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        // Peso
        OutlinedTextField(
            value = peso,
            onValueChange = { if (it.length <= 5) peso = it.filter { c -> c.isDigit() || c == '.' } },
            label = { Text("Peso (kg)") },
            placeholder = { Text("Ej. 32.5") },
            singleLine = true,
            isError = errorPeso != null,
            supportingText = { errorPeso?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Leaf,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        // Estatura
        OutlinedTextField(
            value = estatura,
            onValueChange = { if (it.length <= 5) estatura = it.filter { c -> c.isDigit() || c == '.' } },
            label = { Text("Estatura (cm)") },
            placeholder = { Text("Ej. 135") },
            singleLine = true,
            isError = errorEstatura != null,
            supportingText = { errorEstatura?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Leaf,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(28.dp))

        Button(
            onClick = { validarYContinuar() },
            colors = ButtonDefaults.buttonColors(containerColor = Leaf),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().height(52.dp)
        ) {
            Text("Continuar", style = MaterialTheme.typography.labelLarge, color = Color.White)
        }

        Spacer(Modifier.height(24.dp))
    }
}
