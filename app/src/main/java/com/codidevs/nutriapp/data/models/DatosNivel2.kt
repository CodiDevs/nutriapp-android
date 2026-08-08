package com.codidevs.nutriapp.data.models

import com.codidevs.nutriapp.ui.sendero.ActividadInfo

/**
 * Datos del Nivel 2 "Descubro los nutrientes" según las indicaciones.
 * El nivel tiene 5 actividades.
 */
object DatosNivel2 {

    /** Las 5 actividades del Nivel 2. */
    val ACTIVIDADES = listOf(
        ActividadInfo(1, "🔍", "Descubre los nutrientes"),
        ActividadInfo(2, "🥦", "¿A qué grupo pertenece?"),
        ActividadInfo(3, "✅", "Verdadero o falso"),
        ActividadInfo(4, "✏️", "Completa la frase"),
        ActividadInfo(5, "🍽️", "La mejor opción")
    )

    /** Preguntas de Verdadero/Falso del Nivel 2 (sobre nutrientes). */
    val PREGUNTAS_VF = listOf(
        PreguntaVF("🍎", "Las frutas tienen muchas vitaminas.", true),
        PreguntaVF("🥩", "La carne tiene muchas proteínas.", true),
        PreguntaVF("🥛", "La leche tiene calcio que fortalece los huesos.", true),
        PreguntaVF("🍞", "El arroz nos da carbohidratos (energía).", true),
        PreguntaVF("🍬", "Comer muchos dulces es bueno todos los días.", false),
        PreguntaVF("🥤", "Las gaseosas tienen mucha azúcar.", true),
        PreguntaVF("🥦", "Las verduras tienen fibra que ayuda a la digestión.", true),
        PreguntaVF("🥑", "El aguacate tiene grasas saludables.", true)
    )

    /** Frases incompletas del Nivel 2 (sobre nutrientes). */
    val FRASES = listOf(
        FraseNivel2("🍎", "Las frutas tienen muchas", ".", "vitaminas", listOf("vitaminas", "grasas", "calcio")),
        FraseNivel2("🥛", "La leche tiene mucho", ".", "calcio", listOf("calcio", "hierro", "azúcar")),
        FraseNivel2("🍞", "Los carbohidratos nos dan", ".", "energía", listOf("energía", "calcio", "vitaminas")),
        FraseNivel2("🥦", "Las verduras tienen mucha", ".", "fibra", listOf("fibra", "grasa", "azúcar")),
        FraseNivel2("🍗", "El pollo tiene muchas", ".", "proteínas", listOf("proteínas", "vitaminas", "fibra")),
        FraseNivel2("🥑", "El aguacate tiene grasas", ".", "saludables", listOf("saludables", "azucaradas", "vacías"))
    )

    /** Preguntas de "La mejor opción" del Nivel 2. */
    val MEJOR_OPCION = listOf(
        MejorOpcionNivel2(
            "¿Qué alimento tiene más calcio?",
            "🥛", "Leche", "🍬", "Dulces"
        ),
        MejorOpcionNivel2(
            "¿Qué nos da energía para jugar?",
            "🍞", "Pan y arroz", "🥤", "Gaseosa"
        ),
        MejorOpcionNivel2(
            "¿Qué tiene proteínas para crecer fuerte?",
            "🥚", "Huevo", "🍬", "Caramelo"
        ),
        MejorOpcionNivel2(
            "¿Qué tiene grasas saludables?",
            "🥑", "Aguacate", "🍟", "Papas fritas"
        ),
        MejorOpcionNivel2(
            "¿Qué tiene vitaminas para no enfermarte?",
            "🍊", "Naranja", "🍩", "Dona"
        )
    )
}

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
