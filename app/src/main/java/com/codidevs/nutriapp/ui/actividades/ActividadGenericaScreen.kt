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
    onBack: () -> Unit,
    onTerminada: (puntaje: Int) -> Unit
) {
    when (tipo) {
        "descubre" -> {
            val lista = datos as? List<ItemDescubre>
            DescubreAlimentosScreen(
                alimentos = lista?.map { it.toAlimento() } ?: emptyList(),
                onBack = onBack,
                onTerminada = onTerminada
            )
        }
        "grupos" -> {
            val lista = datos as? List<com.codidevs.nutriapp.data.models.GrupoAlimenticio>
            GrupoPerteneceScreen(
                grupos = lista ?: emptyList(),
                onBack = onBack,
                onTerminada = onTerminada
            )
        }
        "memoria" -> {
            val lista = datos as? List<ParMemoria>
            MemoriaNutritivaScreen(
                pares = lista ?: emptyList(),
                onBack = onBack,
                onTerminada = onTerminada
            )
        }
        "vf" -> {
            val lista = datos as? List<com.codidevs.nutriapp.data.models.PreguntaVF>
            VerdaderoFalsoScreen(
                preguntas = lista ?: emptyList(),
                onBack = onBack,
                onTerminada = onTerminada
            )
        }
        "completa" -> {
            val lista = datos as? List<FraseIncompleta>
            CompletaFraseScreen(
                frases = lista ?: emptyList(),
                onBack = onBack,
                onTerminada = onTerminada
            )
        }
        "mejor_opcion", "situaciones" -> {
            val lista = datos as? List<MejorOpcionNivel2>
            MejorOpcionNivel2Screen(
                preguntas = lista ?: emptyList(),
                onBack = onBack,
                onTerminada = onTerminada
            )
        }
        "ruleta" -> {
            val lista = datos as? List<AlimentoRuleta>
            RuedaAlimentacionScreen(
                alimentos = lista ?: emptyList(),
                onBack = onBack,
                onTerminada = onTerminada
            )
        }
        "quiz" -> {
            val lista = datos as? List<PreguntaQuiz>
            QuizScreen(
                preguntas = lista ?: emptyList(),
                onBack = onBack,
                onTerminada = onTerminada
            )
        }
        "semaforo" -> {
            val dato = datos as? SemaforoDatos
            SemaforoScreen(
                datos = dato ?: SemaforoDatos(emptyList(), emptyList(), emptyList()),
                onBack = onBack,
                onTerminada = onTerminada
            )
        }
        "reto" -> {
            val lista = datos as? List<AccionReto>
            RetoScreen(
                acciones = lista ?: emptyList(),
                onBack = onBack,
                onTerminada = onTerminada
            )
        }
        "une" -> {
            val lista = datos as? List<com.codidevs.nutriapp.data.models.ItemDato>
            UneImagenScreen(
                pares = lista ?: emptyList(),
                onBack = onBack,
                onTerminada = onTerminada
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

/** Item de "grupos" del JSON (por ahora no usado en pantalla). */
data class ItemGrupos(
    val emoji: String,
    val grupo: String
)
