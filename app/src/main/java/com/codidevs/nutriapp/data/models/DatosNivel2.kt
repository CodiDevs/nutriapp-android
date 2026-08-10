package com.codidevs.nutriapp.data.models

data class PreguntaVF(
    val emoji: String,
    val enunciado: String,
    val esVerdadero: Boolean
)

data class FraseNivel2(
    val emoji: String,
    val antes: String,
    val despues: String,
    val respuesta: String,
    val opciones: List<String>
)

data class MejorOpcionNivel2(
    val pregunta: String,
    val emojiCorrecta: String,
    val textoCorrecta: String,
    val emojiIncorrecta: String,
    val textoIncorrecta: String
)
