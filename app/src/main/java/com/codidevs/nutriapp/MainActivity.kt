package com.codidevs.nutriapp

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.codidevs.nutriapp.ui.components.TabScaffold
import com.codidevs.nutriapp.ui.home.HomeScreen
import com.codidevs.nutriapp.ui.juegos.JuegosScreen
import com.codidevs.nutriapp.ui.navigation.NutriRoutes
import com.codidevs.nutriapp.ui.onboarding.ImcScreen
import com.codidevs.nutriapp.ui.onboarding.ModulosScreen
import com.codidevs.nutriapp.ui.onboarding.RegistroScreen
import com.codidevs.nutriapp.ui.onboarding.SplashScreen
import com.codidevs.nutriapp.ui.perfil.PerfilScreen
import com.codidevs.nutriapp.ui.sendero.NIVELES_INFO
import com.codidevs.nutriapp.ui.sendero.NivelDetalleScreen
import com.codidevs.nutriapp.ui.sendero.SenderoScreen
import com.codidevs.nutriapp.ui.theme.NutriAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Barras del sistema (estado y navegación) del color crema de la app,
        // con íconos oscuros para que la hora y los íconos se vean bien.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT)
        )
        setContent {
            NutriAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    var nombreUsuario by remember { mutableStateOf("") }
                    var tabActiva by remember { mutableStateOf("home") }

                    NavHost(
                        navController = navController,
                        startDestination = NutriRoutes.SPLASH,
                        modifier = Modifier
                            .fillMaxSize()
                            .safeDrawingPadding()
                    ) {
                        composable(NutriRoutes.SPLASH) {
                            SplashScreen(onComenzar = {
                                navController.navigate(NutriRoutes.REGISTRO)
                            })
                        }
                        composable(NutriRoutes.REGISTRO) {
                            RegistroScreen(
                                onBack = { navController.popBackStack() },
                                onContinuar = { nombre, edad, peso, estatura ->
                                    // Codificamos los datos para que viajen seguros en la ruta
                                    navController.navigate(
                                        "${NutriRoutes.IMC}/${Uri.encode(nombre)}/$edad/$peso/$estatura"
                                    )
                                }
                            )
                        }
                        composable(
                            "${NutriRoutes.IMC}/{nombre}/{edad}/{peso}/{estatura}"
                        ) { backStackEntry ->
                            val args = backStackEntry.arguments
                            val nombre = args?.getString("nombre").orEmpty()
                            val edad = args?.getString("edad")?.toIntOrNull() ?: 0
                            val peso = args?.getString("peso")?.toDoubleOrNull() ?: 0.0
                            val estatura = args?.getString("estatura")?.toDoubleOrNull() ?: 0.0
                            ImcScreen(
                                nombre = nombre,
                                edad = edad,
                                peso = peso,
                                estatura = estatura,
                                onBack = { navController.popBackStack() },
                                onAventura = {
                                    navController.navigate(
                                        "${NutriRoutes.MODULOS}/${Uri.encode(nombre)}"
                                    )
                                }
                            )
                        }
                        composable(
                            "${NutriRoutes.MODULOS}/{nombre}"
                        ) { backStackEntry ->
                            val nombre = backStackEntry.arguments?.getString("nombre").orEmpty()
                            ModulosScreen(
                                nombre = nombre,
                                onBack = { navController.popBackStack() },
                                onNutricion = {
                                    nombreUsuario = nombre
                                    tabActiva = "home"
                                    navController.navigate(NutriRoutes.HOME)
                                }
                            )
                        }
                        // Las 4 pestañas (Inicio, Sendero, Juegos, Perfil) viven en una sola
                        // pantalla: tocar la barra solo cambia el contenido, sin navegación,
                        // así no se acumulan copias ni se repite la animación de transición.
                        composable(NutriRoutes.HOME) {
                            TabScaffold(
                                tabActiva = tabActiva,
                                onTab = { tabActiva = it }
                            ) {
                                when (tabActiva) {
                                    "sendero" -> SenderoScreen(
                                        onNivelClick = { numero ->
                                            navController.navigate(
                                                "${NutriRoutes.NIVEL_DETALLE}/$numero"
                                            )
                                        }
                                    )
                                    "juegos" -> JuegosScreen()
                                    "perfil" -> PerfilScreen(
                                        nombre = nombreUsuario,
                                        onVerRecompensas = {
                                            navController.navigate(NutriRoutes.RECOMPENSAS)
                                        }
                                    )
                                    else -> HomeScreen(
                                        nombre = nombreUsuario,
                                        onSendero = { tabActiva = "sendero" },
                                        onRecompensas = {
                                            navController.navigate(NutriRoutes.RECOMPENSAS)
                                        },
                                        onPerfil = { tabActiva = "perfil" }
                                    )
                                }
                            }
                        }
                        composable(
                            "${NutriRoutes.NIVEL_DETALLE}/{nivelId}"
                        ) { backStackEntry ->
                            val nivelId = backStackEntry.arguments?.getString("nivelId")
                                ?.toIntOrNull() ?: 1
                            val nivel = NIVELES_INFO.firstOrNull { it.numero == nivelId }
                                ?: NIVELES_INFO.first()
                            NivelDetalleScreen(
                                nivel = nivel,
                                onBack = { navController.popBackStack() },
                                onVerActividades = {
                                    // lista de actividades (siguiente paso)
                                }
                            )
                        }
                        composable(NutriRoutes.RECOMPENSAS) {
                            androidx.compose.material3.Text("Recompensas (próximamente)")
                        }
                    }
                }
            }
        }
    }
}
