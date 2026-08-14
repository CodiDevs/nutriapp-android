package com.codidevs.nutriapp

import com.codidevs.nutriapp.ui.onboarding.ClasificacionImc
import com.codidevs.nutriapp.ui.onboarding.HealthDisclaimer
import com.codidevs.nutriapp.ui.onboarding.calcularImc
import com.codidevs.nutriapp.ui.onboarding.clasificarImc
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PlayComplianceTest {

    @Test
    fun calcularImc_peso32_5_estatura135() {
        val imc = calcularImc(32.5, 135.0)
        assertEquals(17.8, imc, 0.05)
    }

    @Test
    fun clasificarImc_edad8_enRangoNormal() {
        assertEquals(ClasificacionImc.NORMAL, clasificarImc(16.0, 8))
    }

    @Test
    fun clasificarImc_edad8_bajoPeso() {
        assertEquals(ClasificacionImc.BAJO_PESO, clasificarImc(12.0, 8))
    }

    @Test
    fun healthDisclaimer_noEsDiagnostico() {
        assertTrue(HealthDisclaimer.TEXTO.contains("no es un dispositivo médico"))
        assertTrue(HealthDisclaimer.TEXTO.contains("no diagnostica"))
        assertFalse(HealthDisclaimer.TEXTO.contains("OMS"))
    }
}
