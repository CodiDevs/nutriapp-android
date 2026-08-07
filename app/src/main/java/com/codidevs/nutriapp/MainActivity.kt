package com.codidevs.nutriapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.codidevs.nutriapp.ui.navigation.NutriRoutes
import com.codidevs.nutriapp.ui.onboarding.RegistroScreen
import com.codidevs.nutriapp.ui.onboarding.SplashScreen
import com.codidevs.nutriapp.ui.theme.NutriAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NutriAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = NutriRoutes.SPLASH) {
                        composable(NutriRoutes.SPLASH) {
                            SplashScreen(onComenzar = {
                                navController.navigate(NutriRoutes.REGISTRO)
                            })
                        }
                        composable(NutriRoutes.REGISTRO) {
                            RegistroScreen(onContinuar = { nombre, edad, peso, estatura ->
                                // Pasamos los datos como argumentos a la pantalla IMC
                                navController.navigate(
                                    "${NutriRoutes.IMC}/$nombre/$edad/$peso/$estatura"
                                )
                            })
                        }
                        composable(
                            "${NutriRoutes.IMC}/{nombre}/{edad}/{peso}/{estatura}"
                        ) { backStackEntry ->
                            val args = backStackEntry.arguments
                            androidx.compose.material3.Text(
                                "IMC: ${args?.getString("nombre")} · ${args?.getString("edad")} años · " +
                                    "${args?.getString("peso")} kg · ${args?.getString("estatura")} cm (próximamente)"
                            )
                        }
                    }
                }
            }
        }
    }
}
