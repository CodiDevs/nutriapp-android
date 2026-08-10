package com.codidevs.nutriapp.data.repository

import android.content.Context
import android.content.SharedPreferences

/**
 * Guarda el progreso del niño en el dispositivo (SharedPreferences):
 * actividades completadas por nivel, puntaje y monedas.
 * Es genérico: sirve para cualquier nivel/módulo.
 */
class ProgresoRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("progreso_nutriapp", Context.MODE_PRIVATE)

    /**
     * Registra el resultado de una actividad con su desempeño (porcentaje 0-100).
     * Guarda el MEJOR porcentaje de la actividad. Los totales (estrellas y monedas)
     * se calculan a demanda sumando el mejor registro de cada actividad — nunca se
     * acumulan intentos repetidos, solo cuenta el mejor desempeño.
     */
    fun registrarResultadoActividad(
        nivel: Int,
        actividadId: Int,
        porcentaje: Int,
        monedasNivel: Int
    ): Int {
        val clavePorcentaje = "porcentaje_nivel_${nivel}_actividad_${actividadId}"
        val mejorAnterior = prefs.getInt(clavePorcentaje, -1)
        if (porcentaje > mejorAnterior) {
            prefs.edit().putInt(clavePorcentaje, porcentaje).apply()
        }
        // Devuelve las estrellas del mejor porcentaje actual (0-3)
        return estrellasPorPorcentaje(maxOf(mejorAnterior, porcentaje))
    }

    /** Devuelve las estrellas (0-3) según el porcentaje de acierto. */
    fun estrellasPorPorcentaje(porcentaje: Int): Int = when {
        porcentaje >= 100 -> 3
        porcentaje >= 70 -> 2
        porcentaje >= 40 -> 1
        else -> 0
    }

    /** Monedas de un minijuego según sus estrellas de desempeño (1→5, 2→10, 3→15). */
    fun monedasMinijuego(estrellas: Int): Int = when {
        estrellas >= 3 -> 15
        estrellas >= 2 -> 10
        estrellas >= 1 -> 5
        else -> 0
    }

    /** Estrellas del mejor desempeño de una actividad (0-3, -1 si nunca se jugó). */
    fun estrellasActividad(nivel: Int, actividadId: Int): Int {
        val pct = prefs.getInt("porcentaje_nivel_${nivel}_actividad_${actividadId}", -1)
        return if (pct >= 0) estrellasPorPorcentaje(pct) else -1
    }

    /**
     * Estrellas ASIGNADAS a una actividad según su nivel y posición (no según desempeño):
     * cada nivel reparte 3 estrellas entre sus actividades (la primera recibe más).
     * Devuelve el valor asignado solo si la actividad fue completada (1+ estrellas reales),
     * o 0 si no está completada.
     */
    fun estrellasAsignadasActividad(nivel: Int, actividadId: Int, totalActividades: Int): Int {
        val completada = estrellasActividad(nivel, actividadId) > 0
        if (!completada) return 0
        val asignadas = estrellasPorActividad(nivel, totalActividades)
        return asignadas[actividadId] ?: 1
    }

    /** Reparto de estrellas por actividad (3 en total por nivel). */
    fun estrellasPorActividad(nivel: Int, totalActividades: Int): Map<Int, Int> {
        if (totalActividades <= 0) return emptyMap()
        // Niveles con 2 actividades: 2 y 1. Con 3: 1, 1, 1. Con 1: 3.
        val valores = when (totalActividades) {
            1 -> listOf(3)
            2 -> listOf(2, 1)
            else -> List(totalActividades) { 1 }
        }
        return (1..totalActividades).associateWith { valores[it - 1] }
    }

    /** Guarda las estrellas logradas en un minijuego (0-3). */
    fun setEstrellasMinijuego(id: String, estrellas: Int) {
        prefs.edit().putInt("minijuego_estrellas_$id", estrellas).apply()
    }

    /** Estrellas logradas en un minijuego (0-3, -1 si nunca se jugó). */
    fun estrellasMinijuego(id: String): Int =
        prefs.getInt("minijuego_estrellas_$id", -1)

    /** Indica si un minijuego libre está completado (2+ estrellas). */
    fun minijuegoCompletado(id: String): Boolean =
        prefs.getInt("minijuego_estrellas_$id", -1) >= 2

    /** Guarda la medalla puesta en el perfil. */
    fun setMedallaPerfil(id: String) {
        prefs.edit().putString("medalla_perfil", id).apply()
    }

    /** La medalla puesta en el perfil ("" si ninguna). */
    val medallaPerfil: String get() = prefs.getString("medalla_perfil", "") ?: ""

    /**
     * Marca una recompensa como canjeada y registra su costo en "monedas_gastadas".
     * Las monedas visibles salen de monedasTotales(), que descuenta lo gastado,
     * así canjear sí reduce las monedas disponibles.
     */
    fun canjearRecompensa(id: String, costo: Int) {
        prefs.edit()
            .putBoolean("canjeada_$id", true)
            .putInt("monedas_gastadas", prefs.getInt("monedas_gastadas", 0) + costo)
            .apply()
    }

    /** Indica si una recompensa fue canjeada. */
    fun recompensaCanjeada(id: String): Boolean =
        prefs.getBoolean("canjeada_$id", false)

    /** Guarda los datos del usuario registrado. */
    fun guardarUsuario(nombre: String, edad: Int, peso: Double, estatura: Double) {
        prefs.edit()
            .putString("usuario_nombre", nombre)
            .putInt("usuario_edad", edad)
            .putString("usuario_peso", peso.toString())
            .putString("usuario_estatura", estatura.toString())
            .apply()
    }

    val usuarioRegistrado: Boolean get() = prefs.contains("usuario_nombre")

    val usuarioNombre: String get() = prefs.getString("usuario_nombre", "") ?: ""

    /** Borra TODO: usuario y progreso (para crear un registro nuevo). */
    fun borrarTodo() {
        prefs.edit().clear().apply()
    }

    /** Registra un día activo y devuelve la racha actual (días consecutivos). */
    fun registrarDiaActivo(): Int {
        // Calendar/SimpleDateFormat funcionan en API 24+ (java.time.LocalDate no)
        val formato = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.ROOT)
        val hoy = formato.format(java.util.Date())
        val ultimo = prefs.getString("ultimo_dia", "") ?: ""
        var racha = prefs.getInt("racha_dias", 0)
        if (ultimo != hoy) {
            val ayer = java.util.Calendar.getInstance().apply {
                add(java.util.Calendar.DAY_OF_YEAR, -1)
            }.time.let { formato.format(it) }
            racha = if (ultimo == ayer) racha + 1 else 1
            prefs.edit()
                .putString("ultimo_dia", hoy)
                .putInt("racha_dias", racha)
                .apply()
        }
        return racha
    }

    val rachaDias: Int get() = prefs.getInt("racha_dias", 0)

    /** Número de actividades completadas de un nivel (con al menos 1 estrella). */
    fun actividadesCompletadas(nivel: Int): Int =
        (1..10).count { prefs.getInt("porcentaje_nivel_${nivel}_actividad_$it", -1) >= 40 }

    /** El nivel está completo cuando se completan todas sus actividades. */
    fun nivelCompleto(nivel: Int, totalActividades: Int): Boolean =
        actividadesCompletadas(nivel) >= totalActividades

    /**
     * Estrellas totales: suma de las estrellas del mejor desempeño de cada actividad
     * y minijuego (no acumula intentos repetidos).
     */
    fun estrellasTotales(actividadesPorNivel: Map<Int, Int>): Int {
        var total = 0
        actividadesPorNivel.forEach { (nivel, numAct) ->
            for (id in 1..numAct) {
                val pct = prefs.getInt("porcentaje_nivel_${nivel}_actividad_$id", -1)
                if (pct > 0) total += estrellasPorPorcentaje(pct)
            }
        }
        // Minijuegos libres (su mejor registro)
        listOf("arrastrar", "vf", "completa", "mejor", "ruleta", "memoria").forEach { id ->
            val est = prefs.getInt("minijuego_estrellas_$id", -1)
            if (est > 0) total += est
        }
        return total
    }

    /**
     * Monedas totales: suma de las monedas ganadas con el MEJOR desempeño de cada
     * actividad y minijuego (no acumula intentos), menos lo gastado en canjes.
     */
    fun monedasTotales(monedasPorNivel: Map<Int, Int>, actividadesPorNivel: Map<Int, Int>): Int {
        var total = 0
        monedasPorNivel.forEach { (nivel, monedasNivel) ->
            val numAct = actividadesPorNivel[nivel] ?: 0
            for (id in 1..numAct) {
                val pct = prefs.getInt("porcentaje_nivel_${nivel}_actividad_$id", -1)
                if (pct > 0) total += (monedasNivel * pct / 100)
            }
        }
        // Minijuegos libres: monedas según estrellas de desempeño (1→5, 2→10, 3→15)
        listOf("arrastrar", "vf", "completa", "mejor", "ruleta", "memoria").forEach { id ->
            val est = prefs.getInt("minijuego_estrellas_$id", -1)
            if (est > 0) total += monedasMinijuego(est)
        }
        // Descuenta lo gastado en recompensas canjeadas
        return (total - prefs.getInt("monedas_gastadas", 0)).coerceAtLeast(0)
    }
}
