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
import com.codidevs.nutriapp.data.models.CatalogoAlimentos
import com.codidevs.nutriapp.data.models.GruposAlimenticios
import com.codidevs.nutriapp.ui.actividades.CompletaFraseScreen
import com.codidevs.nutriapp.ui.actividades.DescubreAlimentosScreen
import com.codidevs.nutriapp.ui.actividades.GrupoPerteneceScreen
import com.codidevs.nutriapp.ui.actividades.MejorOpcionScreen
import com.codidevs.nutriapp.ui.actividades.MemoriaNutritivaScreen
import com.codidevs.nutriapp.ui.actividades.PremioScreen
import com.codidevs.nutriapp.ui.actividades.RuedaAlimentacionScreen
import com.codidevs.nutriapp.ui.actividades.VerdaderoFalsoScreen
import com.codidevs.nutriapp.ui.components.TabScaffold
import com.codidevs.nutriapp.ui.home.HomeScreen
import com.codidevs.nutriapp.ui.juegos.JuegosScreen
import com.codidevs.nutriapp.ui.navigation.NutriRoutes
import com.codidevs.nutriapp.ui.onboarding.ImcScreen
import com.codidevs.nutriapp.ui.onboarding.ModulosScreen
import com.codidevs.nutriapp.ui.onboarding.RegistroScreen
import com.codidevs.nutriapp.ui.onboarding.SplashScreen
import com.codidevs.nutriapp.ui.perfil.PerfilScreen
import com.codidevs.nutriapp.ui.sendero.ACTIVIDADES_NIVEL_1
import com.codidevs.nutriapp.ui.sendero.ActividadesScreen
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
                                    navController.navigate(
                                        "${NutriRoutes.ACTIVIDADES}/${nivel.numero}"
                                    )
                                }
                            )
                        }
                        composable(
                            "${NutriRoutes.ACTIVIDADES}/{nivelId}"
                        ) { backStackEntry ->
                            val nivelId = backStackEntry.arguments?.getString("nivelId")
                                ?.toIntOrNull() ?: 1
                            ActividadesScreen(
                                nivelNumero = nivelId,
                                onBack = { navController.popBackStack() },
                                onActividadClick = { actividad ->
                                    when (actividad.id) {
                                        1 -> navController.navigate(NutriRoutes.ACTIVIDAD_DESCUBRE)
                                        2 -> navController.navigate(NutriRoutes.ACTIVIDAD_GRUPO)
                                        3 -> navController.navigate(NutriRoutes.ACTIVIDAD_VF)
                                        4 -> navController.navigate(NutriRoutes.ACTIVIDAD_FRASE)
                                        5 -> navController.navigate(NutriRoutes.ACTIVIDAD_MEJOR)
                                        6 -> navController.navigate(NutriRoutes.ACTIVIDAD_RULETA)
                                        7 -> navController.navigate(NutriRoutes.ACTIVIDAD_MEMORIA)
                                    }
                                }
                            )
                        }
                        composable(NutriRoutes.ACTIVIDAD_DESCUBRE) {
                            DescubreAlimentosScreen(
                                alimentos = CatalogoAlimentos.TODOS,
                                onBack = { navController.popBackStack() },
                                onTerminada = { puntaje ->
                                    // El premio reemplaza al minijuego en el stack,
                                    // así "Continuar" regresa a la lista de actividades
                                    navController.navigate(NutriRoutes.PREMIO) {
                                        popUpTo(NutriRoutes.ACTIVIDAD_DESCUBRE) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(NutriRoutes.ACTIVIDAD_GRUPO) {
                            GrupoPerteneceScreen(
                                grupos = GruposAlimenticios.TODOS,
                                onBack = { navController.popBackStack() },
                                onTerminada = { puntaje ->
                                    navController.navigate(NutriRoutes.PREMIO) {
                                        popUpTo(NutriRoutes.ACTIVIDAD_GRUPO) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(NutriRoutes.ACTIVIDAD_VF) {
                            VerdaderoFalsoScreen(
                                onBack = { navController.popBackStack() },
                                onTerminada = { puntaje ->
                                    navController.navigate(NutriRoutes.PREMIO) {
                                        popUpTo(NutriRoutes.ACTIVIDAD_VF) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(NutriRoutes.ACTIVIDAD_FRASE) {
                            CompletaFraseScreen(
                                onBack = { navController.popBackStack() },
                                onTerminada = { puntaje ->
                                    navController.navigate(NutriRoutes.PREMIO) {
                                        popUpTo(NutriRoutes.ACTIVIDAD_FRASE) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(NutriRoutes.ACTIVIDAD_MEJOR) {
                            MejorOpcionScreen(
                                onBack = { navController.popBackStack() },
                                onTerminada = { puntaje ->
                                    navController.navigate(NutriRoutes.PREMIO) {
                                        popUpTo(NutriRoutes.ACTIVIDAD_MEJOR) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(NutriRoutes.ACTIVIDAD_RULETA) {
                            RuedaAlimentacionScreen(
                                onBack = { navController.popBackStack() },
                                onTerminada = { puntaje ->
                                    navController.navigate(NutriRoutes.PREMIO) {
                                        popUpTo(NutriRoutes.ACTIVIDAD_RULETA) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(NutriRoutes.ACTIVIDAD_MEMORIA) {
                            MemoriaNutritivaScreen(
                                onBack = { navController.popBackStack() },
                                onTerminada = { puntaje ->
                                    navController.navigate(NutriRoutes.PREMIO) {
                                        popUpTo(NutriRoutes.ACTIVIDAD_MEMORIA) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(NutriRoutes.PREMIO) {
                            PremioScreen(
                                onContinuar = { navController.popBackStack() }
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
