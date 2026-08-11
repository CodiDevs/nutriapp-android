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

    /** Marca las medallas desbloqueadas según el progreso. */
    fun conProgreso(
        nutricionCompleto: Boolean,
        actividadCompleto: Boolean,
        todosNivelesCompleto: Boolean,
        minijuegosCompleto: Boolean
    ): List<MedallaInfo> {
        val especialDesbloqueada = todosNivelesCompleto && minijuegosCompleto
        return TODAS.map { m ->
            val desbloqueada = when (m.id) {
                "frutas" -> nutricionCompleto
                "verduras" -> nutricionCompleto
                "agua" -> nutricionCompleto && actividadCompleto
                "deportista" -> actividadCompleto
                "corazon" -> actividadCompleto
                "habitos" -> todosNivelesCompleto
                "especial" -> especialDesbloqueada
                else -> false
            }
            // Ahora todas nacen con el candado quitado si el módulo está "desbloqueado",
            // pero la lógica de canjeada (gratis vs pagada) se maneja en el Repository.
            m.copy(desbloqueada = desbloqueada)
        }
    }
}
