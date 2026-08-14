package com.codidevs.nutriapp.data.models

/** Información de una medalla. */
data class MedallaInfo(
    val id: String,
    val emoji: String,
    val nombre: String,
    val descripcion: String,
    val orden: Int,
    val desbloqueada: Boolean = false,
    val especial: Boolean = false
)

/**
 * Catálogo de medallas de la app: las 6 de las indicaciones + la medalla especial
 * (que se desbloquea al completar toda la app: sendero + minijuegos).
 */
object CatalogoMedallas {

    val TODAS = listOf(
        MedallaInfo("frutas", "🥇", "Explorador de las Frutas", "Completa el módulo de Nutrición", 1),
        MedallaInfo("verduras", "🥇", "Rey o Reina de las Verduras", "Completa los niveles de alimentos", 2),
        MedallaInfo("agua", "🥇", "Campeón del Agua", "Completa los niveles de hábitos", 3),
        MedallaInfo("deportista", "🥇", "Super Deportista", "Completa el módulo de Actividad física", 4),
        MedallaInfo("corazon", "🥇", "Protector de su Corazón", "Completa los niveles de ejercicio", 5),
        MedallaInfo("habitos", "🥇", "Guardián de los Buenos Hábitos", "Completa todos los niveles", 6),
        MedallaInfo(
            id = "especial",
            emoji = "🌟",
            nombre = "Medalla Especial NutriHero",
            descripcion = "Completa toda la app: sendero + minijuegos",
            orden = 7,
            especial = true
        )
    )

    /** Marca las medallas desbloqueadas según el progreso granular. */
    fun conProgreso(
        nivelesCompletados: Set<Int>,
        minijuegosCompleto: Boolean
    ): List<MedallaInfo> {
        val todosNiveles = (1..7).all { it in nivelesCompletados }
        val especialDesbloqueada = todosNiveles && minijuegosCompleto
        
        return TODAS.map { m ->
            val desbloqueada = when (m.id) {
                "frutas" -> (1..3).all { it in nivelesCompletados } // Módulo 1 completo
                "verduras" -> 1 in nivelesCompletados
                "agua" -> 5 in nivelesCompletados
                "deportista" -> (4..7).all { it in nivelesCompletados } // Módulo 2 completo
                "corazon" -> 4 in nivelesCompletados
                "habitos" -> (1..7).all { it in nivelesCompletados } // Todos los niveles
                "especial" -> especialDesbloqueada
                else -> false
            }
            m.copy(desbloqueada = desbloqueada)
        }
    }
}
