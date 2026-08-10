package com.codidevs.nutriapp.ui.actividades

import androidx.compose.runtime.Composable
import com.codidevs.nutriapp.data.models.MejorOpcionNivel2

/**
 * Pantalla genérica de actividad: recibe el nivel y la actividad (del JSON),
 * carga los datos según su tipo y muestra el minijuego correspondiente.
 * Al terminar, llama a onTerminada(puntaje) para que MainActivity registre el progreso.
 */
@Composable
fun ActividadGenericaScreen(
    tipo: String,
    datos: Any?,
    titulo: String,
    totalPreguntas: Int,
    onBack: () -> Unit,
    onTerminada: (puntaje: Int, porcentaje: Int) -> Unit
) {
    // Calcula el porcentaje de acierto según el puntaje y el total de preguntas.
    // Los tipos de aprendizaje o sin examen ("descubre", "ruleta", "une", "semaforo",
    // "reto") no tienen fallos reales: al completarlos se da el 100% (3 estrellas).
    val sinFallos = tipo == "descubre" || tipo == "ruleta" || tipo == "une" ||
        tipo == "semaforo" || tipo == "reto"
    val terminar = { puntaje: Int ->
        val porcentaje = if (sinFallos) {
            100
        } else {
            val maximo = totalPreguntas * 10
            if (maximo > 0) (puntaje * 100 / maximo).coerceIn(0, 100) else 0
        }
        onTerminada(puntaje, porcentaje)
    }

    when (tipo) {
        "descubre" -> {
            val lista = datos as? List<ItemDescubre>
            DescubreAlimentosScreen(
                alimentos = lista?.map { it.toAlimento() } ?: emptyList(),
                onBack = onBack,
                onTerminada = terminar
            )
        }
        "grupos" -> {
            val lista = datos as? List<com.codidevs.nutriapp.data.models.GrupoAlimenticio>
            GrupoPerteneceScreen(
                grupos = lista ?: emptyList(),
                onBack = onBack,
                onTerminada = terminar
            )
        }
        "memoria" -> {
            val lista = datos as? List<ParMemoria>
            MemoriaNutritivaScreen(
                pares = lista ?: emptyList(),
                onBack = onBack,
                onTerminada = terminar
            )
        }
        "vf" -> {
            val lista = datos as? List<com.codidevs.nutriapp.data.models.PreguntaVF>
            VerdaderoFalsoScreen(
                preguntas = lista ?: emptyList(),
                onBack = onBack,
                onTerminada = terminar
            )
        }
        "completa" -> {
            val lista = datos as? List<FraseIncompleta>
            CompletaFraseScreen(
                frases = lista ?: emptyList(),
                onBack = onBack,
                onTerminada = terminar
            )
        }
        "mejor_opcion", "situaciones" -> {
            val lista = datos as? List<MejorOpcionNivel2>
            MejorOpcionNivel2Screen(
                preguntas = lista ?: emptyList(),
                onBack = onBack,
                onTerminada = terminar
            )
        }
        "ruleta" -> {
            val lista = datos as? List<AlimentoRuleta>
            RuedaAlimentacionScreen(
                alimentos = lista ?: emptyList(),
                onBack = onBack,
                onTerminada = terminar
            )
        }
        "quiz" -> {
            val lista = datos as? List<PreguntaQuiz>
            QuizScreen(
                preguntas = lista ?: emptyList(),
                onBack = onBack,
                onTerminada = terminar
            )
        }
        "semaforo" -> {
            val dato = datos as? SemaforoDatos
            SemaforoScreen(
                datos = dato ?: SemaforoDatos(emptyList(), emptyList(), emptyList()),
                onBack = onBack,
                onTerminada = terminar
            )
        }
        "reto" -> {
            val lista = datos as? List<AccionReto>
            RetoScreen(
                acciones = lista ?: emptyList(),
                onBack = onBack,
                onTerminada = terminar
            )
        }
        "une" -> {
            val lista = datos as? List<com.codidevs.nutriapp.data.models.ItemDato>
            UneImagenScreen(
                pares = lista ?: emptyList(),
                onBack = onBack,
                onTerminada = terminar
            )
        }
        else -> {
            onBack()
        }
    }
}

/** Item de "descubre" del JSON. */
data class ItemDescubre(
    val emoji: String,
    val nombre: String,
    val texto: String
) {
    fun toAlimento() = com.codidevs.nutriapp.data.models.Alimento(emoji, nombre, texto)
}
