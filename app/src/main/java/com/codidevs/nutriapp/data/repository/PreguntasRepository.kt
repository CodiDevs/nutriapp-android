package com.codidevs.nutriapp.data.repository

import android.content.Context
import com.codidevs.nutriapp.data.models.ActividadJson
import com.codidevs.nutriapp.data.models.ModuloJson
import com.codidevs.nutriapp.data.models.NivelJson
import com.codidevs.nutriapp.data.models.PreguntasJson

/**
 * Lee el contenido de preguntas desde assets/data/preguntas.json.
 * Este JSON es la fuente única del contenido de los niveles.
 */
class PreguntasRepository(context: Context) {

    private val modulos: List<ModuloJson> by lazy {
        val texto = context.assets.open("data/preguntas.json")
            .bufferedReader()
            .use { it.readText() }
        PreguntasJson.parsear(texto)
    }

    val todosLosModulos: List<ModuloJson> get() = modulos

    fun nivel(numero: Int): NivelJson? =
        modulos.flatMap { it.niveles }.firstOrNull { it.numero == numero }

    fun actividadesDelNivel(numero: Int): List<ActividadJson> =
        nivel(numero)?.actividades ?: emptyList()

    fun totalActividadesNivel(numero: Int): Int = actividadesDelNivel(numero).size

    /**
     * Calcula el puntaje máximo que se puede obtener en un nivel sumando
     * el puntaje máximo de cada una de sus actividades.
     */
    fun puntosMaximosNivel(numero: Int): Int {
        val actividades = actividadesDelNivel(numero)
        return actividades.sumOf { puntosMaximosActividad(it) }
    }

    /**
     * Define el puntaje máximo según el tipo de actividad.
     * Basado en la lógica de las pantallas (normalmente 10 puntos por acierto).
     */
    fun puntosMaximosActividad(act: ActividadJson): Int {
        return when (act.tipo) {
            "descubre" -> 10
            "grupos" -> 60 // 6 rondas fijas de arrastre
            "reto" -> {
                var sum = 0
                for (i in 0 until act.datos.length()) {
                    sum += act.datos.getJSONObject(i).optInt("puntos", 0)
                }
                sum
            }
            "semaforo" -> {
                val obj = act.datosObjeto
                if (obj != null) {
                    (obj.getJSONArray("verde").length() +
                        obj.getJSONArray("amarillo").length() +
                        obj.getJSONArray("rojo").length()) * 10
                } else 0
            }
            else -> act.datos.length() * 10
        }
    }
}
