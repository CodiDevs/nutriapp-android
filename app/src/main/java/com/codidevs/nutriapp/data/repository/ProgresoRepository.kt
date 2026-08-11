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
     * Registra el resultado de una actividad con su desempeño (porcentaje 0-100) y puntaje.
     * Guarda el MEJOR porcentaje y el MEJOR puntaje de la actividad.
     */
    fun registrarResultadoActividad(
        nivel: Int,
        actividadId: Int,
        porcentaje: Int,
        puntaje: Int
    ): Int {
        val clavePorcentaje = "porcentaje_nivel_${nivel}_actividad_${actividadId}"
        val mejorAnterior = prefs.getInt(clavePorcentaje, -1)
        if (porcentaje > mejorAnterior) {
            prefs.edit().putInt(clavePorcentaje, porcentaje).apply()
        }
        
        val clavePuntaje = "puntaje_nivel_${nivel}_actividad_${actividadId}"
        val mejorPuntajeAnterior = prefs.getInt(clavePuntaje, -1)
        if (puntaje > mejorPuntajeAnterior) {
            prefs.edit().putInt(clavePuntaje, puntaje).apply()
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

    /** Monedas de un minijuego según sus estrellas de desempeño (1→5, 2→10, 3→20). */
    fun monedasMinijuego(estrellas: Int): Int = when {
        estrellas >= 3 -> 20
        estrellas >= 2 -> 10
        estrellas >= 1 -> 5
        else -> 0
    }

    /** Estrellas del mejor desempeño de una actividad (0-3, -1 si nunca se jugó). */
    fun estrellasActividad(nivel: Int, actividadId: Int): Int {
        val pct = prefs.getInt("porcentaje_nivel_${nivel}_actividad_${actividadId}", -1)
        return if (pct >= 0) estrellasPorPorcentaje(pct) else -1
    }

    /** Porcentaje del mejor desempeño de una actividad (0-100, -1 si nunca se jugó). */
    fun porcentajeActividad(nivel: Int, actividadId: Int): Int =
        prefs.getInt("porcentaje_nivel_${nivel}_actividad_${actividadId}", -1)

    /** Indica si un módulo fue completado perfectamente (100% en todos sus niveles). */
    fun moduloPerfecto(rangoNiveles: IntRange): Boolean {
        return rangoNiveles.all { nivelCompletoAl100(it) }
    }

    /**
     * Estrellas ASIGNADAS a una actividad según su nivel y posición (no según desempeño):
     * cada nivel reparte 3 estrellas entre sus actividades.
     * Devuelve el valor asignado (1 o 2) solo si la actividad fue completada (1+ estrellas reales),
     * o 0 si no está completada.
     */
    fun estrellasAsignadasActividad(nivel: Int, actividadId: Int, totalActividades: Int): Int {
        val completada = estrellasActividad(nivel, actividadId) > 0
        if (!completada) return 0
        val asignadas = estrellasPorActividad(nivel, totalActividades)
        // Por seguridad, si el ID no está en el mapa (raro), devolvemos 0 para no inflar el total
        return asignadas[actividadId] ?: 0
    }

    /** Reparto de estrellas por actividad (siempre suma exactamente 3 por nivel). */
    fun estrellasPorActividad(nivel: Int, totalActividades: Int): Map<Int, Int> {
        if (totalActividades <= 0) return emptyMap()
        val res = mutableMapOf<Int, Int>()
        when (totalActividades) {
            1 -> res[1] = 3
            2 -> { res[1] = 2; res[2] = 1 }
            3 -> { res[1] = 1; res[2] = 1; res[3] = 1 }
            else -> {
                // Para más de 3 actividades, repartimos 1 estrella en la primera, 
                // una en la mitad y una en la última.
                val mitad = (totalActividades / 2) + 1
                for (i in 1..totalActividades) {
                    res[i] = if (i == 1 || i == mitad || i == totalActividades) 1 else 0
                }
            }
        }
        return res
    }

    /** Guarda las estrellas logradas en un minijuego (0-3). */
    fun setEstrellasMinijuego(id: String, estrellas: Int) {
        prefs.edit().putInt("minijuego_estrellas_$id", estrellas).apply()
    }

    /** Guarda el puntaje máximo logrado en un minijuego. */
    fun setPuntajeMinijuego(id: String, puntaje: Int) {
        val actual = prefs.getInt("minijuego_puntaje_$id", 0)
        if (puntaje > actual) {
            prefs.edit().putInt("minijuego_puntaje_$id", puntaje).apply()
        }
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
    fun recompensaCanjeada(id: String): Boolean {
        // Las medallas de "frutas" y "deportista" son automáticas:
        // se consideran "canjeadas" gratis si los módulos están al 100%.
        if (id == "frutas") {
            val completo = (1..3).all { nivelCompletoAl100(it) }
            if (completo) return true
        }
        if (id == "deportista") {
            val completo = (4..7).all { nivelCompletoAl100(it) }
            if (completo) return true
        }
        return prefs.getBoolean("canjeada_$id", false)
    }

    /** Indica si un nivel fue completado perfectamente (todas las actividades al 100%). */
    private fun nivelCompletoAl100(nivel: Int): Boolean {
        val prefsAll = prefs.all
        // Filtramos las claves de porcentaje para ese nivel
        val clavesNivel = prefsAll.keys.filter { it.startsWith("porcentaje_nivel_${nivel}_actividad_") }
        if (clavesNivel.isEmpty()) return false
        
        // Verificamos que todos los porcentajes guardados sean 100
        return clavesNivel.all { (prefsAll[it] as? Int ?: 0) >= 100 }
    }

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

    /**
     * Borra solo el progreso de las actividades (porcentajes, puntajes, estrellas, monedas y medallas),
     * pero MANTIENE los datos del usuario (nombre, edad, etc.) y la racha de días.
     */
    fun borrarSoloProgresoActividades() {
        val keys = prefs.all.keys
        val editor = prefs.edit()
        keys.forEach { key ->
            val esDatoUsuario = key.startsWith("usuario_") || 
                               key == "ultimo_dia" || 
                               key == "racha_dias"
            if (!esDatoUsuario) {
                editor.remove(key)
            }
        }
        editor.apply()
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
    fun actividadesCompletadas(nivel: Int, totalActividades: Int): Int =
        (1..totalActividades).count { prefs.getInt("porcentaje_nivel_${nivel}_actividad_$it", -1) >= 40 }

    /** El nivel está completo cuando se completan todas sus actividades. */
    fun nivelCompleto(nivel: Int, totalActividades: Int): Boolean {
        if (totalActividades <= 0) return false
        val completadas = (1..totalActividades).count { id ->
            prefs.getInt("porcentaje_nivel_${nivel}_actividad_$id", -1) >= 40
        }
        return completadas >= totalActividades
    }

    /**
     * Estrellas totales: suma de las estrellas ASIGNADAS (proporcionales al nivel)
     * de cada actividad completada y las estrellas de los minijuegos.
     * Así, cada nivel del sendero suma exactamente 3 estrellas al total.
     */
    fun estrellasTotales(actividadesPorNivel: Map<Int, Int>): Int {
        var total = 0
        actividadesPorNivel.forEach { (nivel, numAct) ->
            for (id in 1..numAct) {
                total += estrellasAsignadasActividad(nivel, id, numAct)
            }
        }
        // Minijuegos libres (su mejor registro 0-3)
        listOf("arrastrar", "vf", "completa", "mejor", "ruleta", "memoria").forEach { id ->
            val est = prefs.getInt("minijuego_estrellas_$id", -1)
            if (est > 0) total += est
        }
        return total
    }

    /**
     * Monedas totales: suma de las monedas ganadas con el MEJOR desempeño de cada
     * actividad (fijo 20 por actividad de sendero) y minijuego, menos lo gastado.
     */
    fun monedasTotales(actividadesPorNivel: Map<Int, Int>): Int {
        var total = 0
        actividadesPorNivel.forEach { (nivel, numAct) ->
            for (id in 1..numAct) {
                val pct = prefs.getInt("porcentaje_nivel_${nivel}_actividad_$id", -1)
                // Se dan 20 monedas fijas por completar la actividad al 100%
                if (pct > 0) total += (20 * pct / 100)
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

    /**
     * Puntos totales: suma de los mejores puntajes obtenidos en cada actividad
     * y minijuego. Son independientes de las monedas.
     */
    fun puntosTotales(actividadesPorNivel: Map<Int, Int>): Int {
        var total = 0
        actividadesPorNivel.forEach { (nivel, numAct) ->
            for (id in 1..numAct) {
                val p = prefs.getInt("puntaje_nivel_${nivel}_actividad_$id", 0)
                total += p
            }
        }
        // Puntos de minijuegos libres
        listOf("arrastrar", "vf", "completa", "mejor", "ruleta", "memoria").forEach { id ->
            total += prefs.getInt("minijuego_puntaje_$id", 0)
        }
        return total
    }
}
