package com.codidevs.nutriapp.data.models

/** Progreso de una actividad (para mostrar el estado en la lista). */
data class ActividadProgreso(
    val actividadId: Int,
    val nivel: Int,
    val completada: Boolean
)

/** Progreso de un nivel (para el sendero y detalle). */
data class NivelProgreso(
    val nivel: Int,
    val completadas: Int,
    val total: Int,
    val completo: Boolean
)
