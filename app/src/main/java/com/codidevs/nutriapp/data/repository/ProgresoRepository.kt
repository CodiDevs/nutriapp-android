package com.codidevs.nutriapp.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.codidevs.nutriapp.data.models.ActividadProgreso
import com.codidevs.nutriapp.data.models.NivelProgreso

/**
 * Guarda el progreso del niño en el dispositivo (SharedPreferences):
 * actividades completadas por nivel, puntaje y monedas.
 * Es genérico: sirve para cualquier nivel/módulo.
 */
class ProgresoRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("progreso_nutriapp", Context.MODE_PRIVATE)

    /** Marca una actividad como completada y suma puntos/monedas (solo la primera vez). */
    fun completarActividad(nivel: Int, actividadId: Int, puntos: Int, monedas: Int) {
        val clave = "nivel_${nivel}_actividad_${actividadId}"
        if (prefs.getBoolean(clave, false)) return // ya estaba completada
        prefs.edit()
            .putBoolean(clave, true)
            .putInt("puntaje_total", prefs.getInt("puntaje_total", 0) + puntos)
            .putInt("monedas_total", prefs.getInt("monedas_total", 0) + monedas)
            .apply()
    }

    /** Suma monedas/puntos sin marcar ninguna actividad (para los minijuegos libres). */
    fun sumarRecompensa(puntos: Int, monedas: Int) {
        prefs.edit()
            .putInt("puntaje_total", prefs.getInt("puntaje_total", 0) + puntos)
            .putInt("monedas_total", prefs.getInt("monedas_total", 0) + monedas)
            .apply()
    }

    /** Marca un minijuego libre como completado (por su id). */
    fun completarMinijuego(id: String) {
        prefs.edit().putBoolean("minijuego_$id", true).apply()
    }

    /** Indica si un minijuego libre está completado. */
    fun minijuegoCompletado(id: String): Boolean =
        prefs.getBoolean("minijuego_$id", false)

    /** Guarda la medalla puesta en el perfil. */
    fun setMedallaPerfil(id: String) {
        prefs.edit().putString("medalla_perfil", id).apply()
    }

    /** La medalla puesta en el perfil ("" si ninguna). */
    val medallaPerfil: String get() = prefs.getString("medalla_perfil", "") ?: ""

    /** Marca una recompensa como canjeada. */
    fun canjearRecompensa(id: String, costo: Int) {
        prefs.edit()
            .putBoolean("canjeada_$id", true)
            .putInt("monedas_total", (prefs.getInt("monedas_total", 0) - costo).coerceAtLeast(0))
            .apply()
    }

    /** Indica si una recompensa fue canjeada. */
    fun recompensaCanjeada(id: String): Boolean =
        prefs.getBoolean("canjeada_$id", false)

    /** Indica si una actividad está completada. */
    fun actividadCompletada(nivel: Int, actividadId: Int): Boolean =
        prefs.getBoolean("nivel_${nivel}_actividad_${actividadId}", false)

    /** Número de actividades completadas de un nivel. */
    fun actividadesCompletadas(nivel: Int): Int =
        (1..10).count { prefs.getBoolean("nivel_${nivel}_actividad_$it", false) }

    /** El nivel está completo cuando se completan todas sus actividades. */
    fun nivelCompleto(nivel: Int, totalActividades: Int): Boolean =
        actividadesCompletadas(nivel) >= totalActividades

    val puntajeTotal: Int get() = prefs.getInt("puntaje_total", 0)
    val monedasTotal: Int get() = prefs.getInt("monedas_total", 0)

    /** Progreso de un nivel (para la UI). */
    fun progresoNivel(nivel: Int, totalActividades: Int): NivelProgreso {
        val completadas = actividadesCompletadas(nivel)
        return NivelProgreso(
            nivel = nivel,
            completadas = completadas,
            total = totalActividades,
            completo = completadas >= totalActividades
        )
    }

    /** Progreso de una actividad (para la lista). */
    fun progresoActividad(nivel: Int, actividadId: Int): ActividadProgreso {
        val completada = actividadCompletada(nivel, actividadId)
        return ActividadProgreso(
            actividadId = actividadId,
            nivel = nivel,
            completada = completada
        )
    }
}
