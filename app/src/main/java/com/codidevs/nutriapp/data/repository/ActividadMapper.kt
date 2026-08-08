package com.codidevs.nutriapp.data.repository

import com.codidevs.nutriapp.data.models.ActividadJson
import com.codidevs.nutriapp.data.models.ItemDato
import com.codidevs.nutriapp.ui.actividades.AccionReto
import com.codidevs.nutriapp.ui.actividades.AlimentoRuleta
import com.codidevs.nutriapp.ui.actividades.AlimentoSemaforo
import com.codidevs.nutriapp.ui.actividades.PreguntaQuiz
import com.codidevs.nutriapp.ui.actividades.SemaforoDatos
import com.codidevs.nutriapp.data.models.PreguntaVF
import com.codidevs.nutriapp.data.models.FraseNivel2
import com.codidevs.nutriapp.data.models.MejorOpcionNivel2
import com.codidevs.nutriapp.ui.actividades.ParMemoria
import org.json.JSONObject

/**
 * Convierte las actividades del JSON (por tipo) en los datos que cada pantalla
 * necesita. Es el puente entre el contenido (assets) y la UI.
 */
object ActividadMapper {

    /** Convierte los datos de "descubre" en items. */
    fun descubre(act: ActividadJson): List<com.codidevs.nutriapp.ui.actividades.ItemDescubre> =
        (0 until act.datos.length()).map { i ->
            val o = act.datos.getJSONObject(i)
            com.codidevs.nutriapp.ui.actividades.ItemDescubre(
                emoji = o.getString("emoji"),
                nombre = o.getString("nombre"),
                texto = o.getString("texto")
            )
        }

    /** Convierte el array "datos" de una actividad de tipo V/F en preguntas. */
    fun preguntasVF(act: ActividadJson): List<PreguntaVF> =
        (0 until act.datos.length()).map { i ->
            val o = act.datos.getJSONObject(i)
            PreguntaVF(
                emoji = o.getString("emoji"),
                enunciado = o.getString("enunciado"),
                esVerdadero = o.getBoolean("verdadero")
            )
        }

    /** Convierte los datos de "completa" en frases. */
    fun frases(act: ActividadJson): List<FraseNivel2> =
        (0 until act.datos.length()).map { i ->
            val o = act.datos.getJSONObject(i)
            FraseNivel2(
                emoji = o.getString("emoji"),
                antes = o.getString("antes"),
                despues = o.getString("despues"),
                respuesta = o.getString("respuesta"),
                opciones = (0 until o.getJSONArray("opciones").length())
                    .map { j -> o.getJSONArray("opciones").getString(j) }
            )
        }

    /** Convierte los datos de "mejor_opcion" en preguntas de mejor opción. */
    fun mejorOpcion(act: ActividadJson): List<MejorOpcionNivel2> =
        (0 until act.datos.length()).map { i ->
            val o = act.datos.getJSONObject(i)
            val correcta = o.getJSONObject("correcta")
            val incorrecta = o.getJSONObject("incorrecta")
            MejorOpcionNivel2(
                pregunta = o.getString("pregunta"),
                emojiCorrecta = correcta.getString("emoji"),
                textoCorrecta = correcta.getString("texto"),
                emojiIncorrecta = incorrecta.getString("emoji"),
                textoIncorrecta = incorrecta.getString("texto")
            )
        }

    /** Convierte los datos de "ruleta" en alimentos de la rueda. */
    fun ruleta(act: ActividadJson): List<AlimentoRuleta> =
        (0 until act.datos.length()).map { i ->
            val o = act.datos.getJSONObject(i)
            AlimentoRuleta(
                emoji = o.getString("emoji"),
                nombre = o.getString("nombre"),
                aporte = o.getString("aporte"),
                opciones = (0 until o.getJSONArray("opciones").length())
                    .map { j -> o.getJSONArray("opciones").getString(j) }
            )
        }

    /** Convierte los datos de "memoria" en parejas. */
    fun memoria(act: ActividadJson): List<ParMemoria> =
        (0 until act.datos.length()).map { i ->
            val o = act.datos.getJSONObject(i)
            ParMemoria(
                emoji = o.getString("emoji"),
                texto = o.getString("texto")
            )
        }

    /** Convierte los datos de "quiz" en preguntas de opción múltiple. */
    fun quiz(act: ActividadJson): List<PreguntaQuiz> =
        (0 until act.datos.length()).map { i ->
            val o = act.datos.getJSONObject(i)
            PreguntaQuiz(
                pregunta = o.getString("pregunta"),
                correcta = o.getString("correcta"),
                incorrectas = (0 until o.getJSONArray("incorrectas").length())
                    .map { j -> o.getJSONArray("incorrectas").getString(j) }
            )
        }

    /** Convierte los datos de "semaforo" (objeto con verde/amarillo/rojo). */
    fun semaforo(act: ActividadJson): SemaforoDatos {
        val o = act.datosObjeto ?: return SemaforoDatos(emptyList(), emptyList(), emptyList())
        return SemaforoDatos(
            verde = listaAlimentos(o.getJSONArray("verde")),
            amarillo = listaAlimentos(o.getJSONArray("amarillo")),
            rojo = listaAlimentos(o.getJSONArray("rojo"))
        )
    }

    /** Convierte los datos de "reto" en acciones. */
    fun reto(act: ActividadJson): List<AccionReto> =
        (0 until act.datos.length()).map { i ->
            val o = act.datos.getJSONObject(i)
            AccionReto(
                emoji = o.getString("emoji"),
                accion = o.getString("accion"),
                puntos = o.getInt("puntos")
            )
        }

    /** Convierte los datos de "une" (imagen-beneficio) en pares emoji-texto. */
    fun une(act: ActividadJson): List<ItemDato> =
        (0 until act.datos.length()).map { i ->
            val o = act.datos.getJSONObject(i)
            ItemDato(
                emoji = o.getString("emoji"),
                texto = o.getString("texto"),
                nombre = o.optString("nombre", "")
            )
        }

    private fun listaAlimentos(arr: org.json.JSONArray): List<AlimentoSemaforo> =
        (0 until arr.length()).map { i ->
            val o = arr.getJSONObject(i)
            AlimentoSemaforo(
                emoji = o.getString("emoji"),
                nombre = o.getString("nombre")
            )
        }
}
