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
}
