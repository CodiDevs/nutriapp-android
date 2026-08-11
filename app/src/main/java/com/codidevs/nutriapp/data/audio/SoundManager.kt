package com.codidevs.nutriapp.data.audio

import android.media.AudioManager
import android.media.ToneGenerator

/**
 * Gestor central de sonidos efímeros de la app (beeps/tone) con un ToneGenerator.
 *
 * Reúne en un único lugar el sonido que la ruleta ya reproducía (TONE_PROP_BEEP)
 * para reutilizarlo como click de botones y opciones, y expone también los
 * sonidos de giro/parada de la ruleta.
 *
 * Nota: cuando se integren SFX reales (click + ruleta), migrar este singleton a
 * SoundPool + assets es un cambio localizado aquí.
 */
object SoundManager {

    private var toneGenerator: ToneGenerator? = null

    /** Inicializa el ToneGenerator. Llamar una vez en onCreate de MainActivity. */
    fun init() {
        if (toneGenerator == null) {
            toneGenerator = try {
                ToneGenerator(AudioManager.STREAM_MUSIC, 90)
            } catch (_: Exception) {
                null
            }
        }
    }

    /**
     * Click de botón/opción: el mismo beep corto que suena al girar la ruleta.
     * Se reproduce en cada interacción táctil para dar feedback inmediato.
     */
    fun click() {
        toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 100)
    }

    /** Sonido de "tic" rápido cuando la ruleta pasa por un sector. */
    fun ruletaTick() {
        toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 30)
    }

    /** Sonido de inicio de giro de la ruleta. */
    fun ruletaGiro() {
        toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 150)
    }

    /** Sonido de detención de la ruleta. */
    fun ruletaParo() {
        toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 200)
    }

    /** Libera el recurso. Llamar en caso de necesidad (el ToneGenerator es ligero). */
    fun release() {
        toneGenerator?.release()
        toneGenerator = null
    }
}

/**
 * Helper: envuelve un onClick para reproducir el click de botón antes de ejecutarlo.
 * Útil para botones, tarjetas y superficies táctiles sin repetir el llamado al sonido.
 */
fun onClickConSonido(block: () -> Unit): () -> Unit = {
    SoundManager.click()
    block()
}
