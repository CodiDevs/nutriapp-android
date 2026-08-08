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
import com.codidevs.nutriapp.data.models.CatalogoMedallas
import com.codidevs.nutriapp.data.models.CatalogoNutrientes
import com.codidevs.nutriapp.data.models.DatosNivel2
import com.codidevs.nutriapp.data.models.GruposAlimenticios
import com.codidevs.nutriapp.data.repository.ActividadMapper
import com.codidevs.nutriapp.data.repository.PreguntasRepository
import com.codidevs.nutriapp.ui.actividades.ActividadGenericaScreen
import com.codidevs.nutriapp.ui.actividades.CompletaFraseScreen
import com.codidevs.nutriapp.ui.actividades.FRASES_NIVEL1
import com.codidevs.nutriapp.ui.actividades.PREGUNTAS_VF_NIVEL1
import com.codidevs.nutriapp.ui.actividades.DescubreAlimentosScreen
import com.codidevs.nutriapp.ui.actividades.DescubreNutrientesScreen
import com.codidevs.nutriapp.ui.actividades.GrupoPerteneceScreen
import com.codidevs.nutriapp.ui.actividades.MejorOpcionNivel2Screen
import com.codidevs.nutriapp.ui.actividades.MejorOpcionScreen
import com.codidevs.nutriapp.ui.actividades.MemoriaNutritivaScreen
import com.codidevs.nutriapp.ui.actividades.PremioScreen
import com.codidevs.nutriapp.ui.actividades.QuizScreen
import com.codidevs.nutriapp.ui.actividades.RetoScreen
import com.codidevs.nutriapp.ui.actividades.RuedaAlimentacionScreen
import com.codidevs.nutriapp.ui.actividades.SemaforoDatos
import com.codidevs.nutriapp.ui.actividades.SemaforoScreen
import com.codidevs.nutriapp.ui.actividades.VerdaderoFalsoScreen
import com.codidevs.nutriapp.ui.components.TabScaffold
import com.codidevs.nutriapp.ui.home.HomeScreen
import com.codidevs.nutriapp.ui.juegos.JuegosScreen
import com.codidevs.nutriapp.ui.navigation.NutriRoutes
import com.codidevs.nutriapp.data.repository.ProgresoRepository
import com.codidevs.nutriapp.ui.onboarding.ImcScreen
import com.codidevs.nutriapp.ui.onboarding.ModulosScreen
import com.codidevs.nutriapp.ui.onboarding.RegistroScreen
import com.codidevs.nutriapp.ui.onboarding.SplashScreen
import com.codidevs.nutriapp.ui.perfil.PerfilScreen
import com.codidevs.nutriapp.ui.recompensas.RecompensasScreen
import com.codidevs.nutriapp.ui.sendero.ACTIVIDADES_NIVEL_1
import com.codidevs.nutriapp.ui.sendero.ActividadInfo
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
                    val progreso = remember { ProgresoRepository(applicationContext) }
                    val preguntas = remember { PreguntasRepository(applicationContext) }
                    var nombreUsuario by remember { mutableStateOf("") }
                    var tabActiva by remember { mutableStateOf("home") }
                    var moduloActual by remember { mutableStateOf(1) } // 1 = Nutrición, 2 = Actividad física
                    // Refresca la UI cuando cambia el progreso
                    var versionProgreso by remember { mutableStateOf(0) }

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
                                nutricionCompletado = progreso.nivelCompleto(1, preguntas.totalActividadesNivel(1)) &&
                                    progreso.nivelCompleto(2, preguntas.totalActividadesNivel(2)) &&
                                    progreso.nivelCompleto(3, preguntas.totalActividadesNivel(3)),
                                actividadCompletado = progreso.nivelCompleto(4, preguntas.totalActividadesNivel(4)) &&
                                    progreso.nivelCompleto(5, preguntas.totalActividadesNivel(5)) &&
                                    progreso.nivelCompleto(6, preguntas.totalActividadesNivel(6)) &&
                                    progreso.nivelCompleto(7, preguntas.totalActividadesNivel(7)),
                                nivelNutricion = 1 +
                                    (if (progreso.nivelCompleto(1, preguntas.totalActividadesNivel(1))) 1 else 0) +
                                    (if (progreso.nivelCompleto(2, preguntas.totalActividadesNivel(2))) 1 else 0),
                                onBack = { navController.popBackStack() },
                                onNutricion = {
                                    nombreUsuario = nombre
                                    moduloActual = 1
                                    tabActiva = "sendero"
                                    navController.navigate(NutriRoutes.HOME)
                                },
                                onActividadFisica = {
                                    nombreUsuario = nombre
                                    moduloActual = 2
                                    tabActiva = "sendero"
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
                                onTab = { tabActiva = it },
                                monedas = "🪙 ${progreso.monedasTotal}",
                                racha = "🔥 5"
                            ) {
                                when (tabActiva) {
                                    "sendero" -> SenderoScreen(
                                        modulo = moduloActual,
                                        nivelesDesbloqueados = 1 +
                                            (if (progreso.nivelCompleto(1, preguntas.totalActividadesNivel(1))) 1 else 0) +
                                            (if (progreso.nivelCompleto(2, preguntas.totalActividadesNivel(2))) 1 else 0) +
                                            (if (progreso.nivelCompleto(3, preguntas.totalActividadesNivel(3))) 1 else 0) +
                                            (if (progreso.nivelCompleto(4, preguntas.totalActividadesNivel(4))) 1 else 0) +
                                            (if (progreso.nivelCompleto(5, preguntas.totalActividadesNivel(5))) 1 else 0) +
                                            (if (progreso.nivelCompleto(6, preguntas.totalActividadesNivel(6))) 1 else 0),
                                        onNivelClick = { numero ->
                                            navController.navigate(
                                                "${NutriRoutes.NIVEL_DETALLE}/$numero"
                                            )
                                        },
                                        onElegirModulo = {
                                            // Va a la pantalla de elegir módulo
                                            navController.navigate(
                                                "${NutriRoutes.MODULOS}/${Uri.encode(nombreUsuario)}"
                                            )
                                        },
                                        onCambiarModulo = {
                                            // Cambia de módulo y va a la pantalla de elegir
                                            navController.navigate(
                                                "${NutriRoutes.MODULOS}/${Uri.encode(nombreUsuario)}"
                                            )
                                        }
                                    )
                                    "juegos" -> JuegosScreen(
                                        completados = remember(versionProgreso) {
                                            listOf("arrastrar", "vf", "completa", "mejor", "ruleta", "memoria")
                                                .filter { progreso.minijuegoCompletado(it) }
                                                .toSet()
                                        },
                                        onMinijuegoClick = { id ->
                                            val ruta = when (id) {
                                                "arrastrar" -> NutriRoutes.JUEGO_ARRASTRAR
                                                "vf" -> NutriRoutes.JUEGO_VF
                                                "completa" -> NutriRoutes.JUEGO_COMPLETA
                                                "mejor" -> NutriRoutes.JUEGO_MEJOR
                                                "ruleta" -> NutriRoutes.JUEGO_RULETA
                                                "memoria" -> NutriRoutes.JUEGO_MEMORIA
                                                else -> null
                                            }
                                            ruta?.let { navController.navigate(it) }
                                        }
                                    )
                                    "perfil" -> {
                                        val nutricionCompleto = progreso.nivelCompleto(1, preguntas.totalActividadesNivel(1)) &&
                                            progreso.nivelCompleto(2, preguntas.totalActividadesNivel(2)) &&
                                            progreso.nivelCompleto(3, preguntas.totalActividadesNivel(3))
                                        val actividadCompleto = progreso.nivelCompleto(4, preguntas.totalActividadesNivel(4)) &&
                                            progreso.nivelCompleto(5, preguntas.totalActividadesNivel(5)) &&
                                            progreso.nivelCompleto(6, preguntas.totalActividadesNivel(6)) &&
                                            progreso.nivelCompleto(7, preguntas.totalActividadesNivel(7))
                                        val todosNiveles = (1..7).all { progreso.nivelCompleto(it, preguntas.totalActividadesNivel(it)) }
                                        val minijuegosCompleto = listOf("arrastrar", "vf", "completa", "mejor", "ruleta", "memoria")
                                            .all { progreso.minijuegoCompletado(it) }
                                        PerfilScreen(
                                            nombre = nombreUsuario,
                                            nivel = nivelActualGlobal(progreso, preguntas),
                                            medallas = remember(versionProgreso) {
                                                CatalogoMedallas.conProgreso(
                                                    nutricionCompleto,
                                                    actividadCompleto,
                                                    todosNiveles,
                                                    minijuegosCompleto
                                                )
                                            },
                                            medallaPerfil = progreso.medallaPerfil,
                                            onPonerMedalla = { id ->
                                                progreso.setMedallaPerfil(id)
                                                versionProgreso++
                                            },
                                            onVerRecompensas = {
                                                navController.navigate(NutriRoutes.RECOMPENSAS)
                                            }
                                        )
                                    }
                                    else -> HomeScreen(
                                        nombre = nombreUsuario,
                                        nivelTexto = nivelActualTexto(progreso, preguntas, moduloActual),
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
                            // Datos del nivel desde el JSON
                            val nivelJson = preguntas.nivel(nivelId)
                            val nivel = NIVELES_INFO.firstOrNull { it.numero == nivelId }
                                ?: NIVELES_INFO.first()
                            val nivelInfo = nivelJson?.let { n ->
                                nivel.copy(
                                    titulo = n.titulo,
                                    descripcion = n.descripcion,
                                    actividades = n.actividades.size,
                                    monedas = "+${n.monedas}"
                                )
                            } ?: nivel
                            NivelDetalleScreen(
                                nivel = nivelInfo,
                                onBack = { navController.popBackStack() },
                                onVerActividades = {
                                    navController.navigate(
                                        "${NutriRoutes.ACTIVIDADES}/${nivelInfo.numero}"
                                    )
                                }
                            )
                        }
                        composable(
                            "${NutriRoutes.ACTIVIDADES}/{nivelId}"
                        ) { backStackEntry ->
                            val nivelId = backStackEntry.arguments?.getString("nivelId")
                                ?.toIntOrNull() ?: 1
                            // Actividades del nivel desde el JSON
                            val actividadesJson = remember(nivelId) {
                                preguntas.actividadesDelNivel(nivelId)
                            }
                            val actividadesNivel = remember(actividadesJson) {
                                actividadesJson.map { act ->
                                    ActividadInfo(act.id, act.emoji, act.nombre)
                                }
                            }
                            // Set de actividades completadas del nivel
                            val completadas = remember(nivelId, versionProgreso) {
                                actividadesNivel
                                    .filter { progreso.actividadCompletada(nivelId, it.id) }
                                    .map { it.id }
                                    .toSet()
                            }
                            ActividadesScreen(
                                nivelNumero = nivelId,
                                actividades = actividadesNivel,
                                completadas = completadas,
                                onBack = { navController.popBackStack() },
                                onActividadClick = { actividad ->
                                    // Navega a la ruta genérica con nivel y actividad
                                    navController.navigate(
                                        "${NutriRoutes.ACTIVIDAD}/${nivelId}/${actividad.id}"
                                    )
                                },
                                onNivelCompletado = {
                                    // Va al sendero para seguir con los otros niveles
                                    tabActiva = "sendero"
                                    navController.popBackStack(NutriRoutes.HOME, false)
                                }
                            )
                        }
                        composable(
                            "${NutriRoutes.ACTIVIDAD}/{nivelId}/{actividadId}"
                        ) { backStackEntry ->
                            val nivelId = backStackEntry.arguments?.getString("nivelId")
                                ?.toIntOrNull() ?: 1
                            val actividadId = backStackEntry.arguments?.getString("actividadId")
                                ?.toIntOrNull() ?: 1
                            val actJson = preguntas.actividadesDelNivel(nivelId)
                                .firstOrNull { it.id == actividadId }

                            // Mapea los datos según el tipo de la actividad
                            val datos: Any? = when (actJson?.tipo) {
                                "descubre" -> actJson?.let {
                                    com.codidevs.nutriapp.data.repository.ActividadMapper.descubre(it)
                                }
                                "grupos" -> GruposAlimenticios.TODOS
                                "memoria" -> actJson?.let {
                                    com.codidevs.nutriapp.data.repository.ActividadMapper.memoria(it)
                                }
                                "une" -> actJson?.let {
                                    com.codidevs.nutriapp.data.repository.ActividadMapper.une(it)
                                }
                                "vf" -> actJson?.let {
                                    com.codidevs.nutriapp.data.repository.ActividadMapper.preguntasVF(it)
                                }
                                "completa" -> actJson?.let {
                                    com.codidevs.nutriapp.data.repository.ActividadMapper.frases(it).map { frase ->
                                        com.codidevs.nutriapp.ui.actividades.FraseIncompleta(
                                            emoji = frase.emoji,
                                            fraseAntes = frase.antes,
                                            fraseDespues = frase.despues,
                                            respuesta = frase.respuesta,
                                            opciones = frase.opciones
                                        )
                                    }
                                }
                                "mejor_opcion", "situaciones" -> actJson?.let {
                                    com.codidevs.nutriapp.data.repository.ActividadMapper.mejorOpcion(it)
                                }
                                "ruleta" -> actJson?.let {
                                    com.codidevs.nutriapp.data.repository.ActividadMapper.ruleta(it)
                                }
                                "quiz" -> actJson?.let {
                                    com.codidevs.nutriapp.data.repository.ActividadMapper.quiz(it)
                                }
                                "semaforo" -> actJson?.let {
                                    com.codidevs.nutriapp.data.repository.ActividadMapper.semaforo(it)
                                }
                                "reto" -> actJson?.let {
                                    com.codidevs.nutriapp.data.repository.ActividadMapper.reto(it)
                                }
                                else -> null
                            }

                            ActividadGenericaScreen(
                                tipo = actJson?.tipo ?: "",
                                datos = datos,
                                titulo = actJson?.nombre ?: "Actividad",
                                onBack = { navController.popBackStack() },
                                onTerminada = { puntaje ->
                                    val monedas = preguntas.nivel(nivelId)?.monedas ?: 20
                                    progreso.completarActividad(nivelId, actividadId, puntaje, monedas)
                                    versionProgreso++
                                    navController.navigate(NutriRoutes.PREMIO) {
                                        popUpTo("${NutriRoutes.ACTIVIDAD}/$nivelId/$actividadId") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(NutriRoutes.ACTIVIDAD_DESCUBRE) {
                            DescubreAlimentosScreen(
                                alimentos = CatalogoAlimentos.TODOS,
                                onBack = { navController.popBackStack() },
                                onTerminada = { puntaje ->
                                    progreso.completarActividad(1, 1, puntaje, 20)
                                    versionProgreso++
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
                                    progreso.completarActividad(1, 2, puntaje, 20)
                                    versionProgreso++
                                    navController.navigate(NutriRoutes.PREMIO) {
                                        popUpTo(NutriRoutes.ACTIVIDAD_GRUPO) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(NutriRoutes.ACTIVIDAD_VF) {
                            val vfData = preguntas.actividadesDelNivel(2)
                                .firstOrNull { it.tipo == "vf" }
                            VerdaderoFalsoScreen(
                                preguntas = vfData?.let { ActividadMapper.preguntasVF(it) } ?: emptyList(),
                                onBack = { navController.popBackStack() },
                                onTerminada = { puntaje ->
                                    progreso.completarActividad(2, vfData?.id ?: 1, puntaje, 20)
                                    versionProgreso++
                                    navController.navigate(NutriRoutes.PREMIO) {
                                        popUpTo(NutriRoutes.ACTIVIDAD_VF) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(NutriRoutes.ACTIVIDAD_FRASE) {
                            val fraseData = preguntas.actividadesDelNivel(2)
                                .firstOrNull { it.tipo == "completa" }
                            CompletaFraseScreen(
                                frases = fraseData?.let { act ->
                                    ActividadMapper.frases(act).map { frase ->
                                        com.codidevs.nutriapp.ui.actividades.FraseIncompleta(
                                            emoji = frase.emoji,
                                            fraseAntes = frase.antes,
                                            fraseDespues = frase.despues,
                                            respuesta = frase.respuesta,
                                            opciones = frase.opciones
                                        )
                                    }
                                } ?: emptyList(),
                                onBack = { navController.popBackStack() },
                                onTerminada = { puntaje ->
                                    progreso.completarActividad(2, fraseData?.id ?: 2, puntaje, 20)
                                    versionProgreso++
                                    navController.navigate(NutriRoutes.PREMIO) {
                                        popUpTo(NutriRoutes.ACTIVIDAD_FRASE) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(NutriRoutes.ACTIVIDAD_MEJOR) {
                            val mejorData = preguntas.actividadesDelNivel(3)
                                .firstOrNull { it.tipo == "mejor_opcion" }
                            MejorOpcionNivel2Screen(
                                preguntas = mejorData?.let { ActividadMapper.mejorOpcion(it) } ?: emptyList(),
                                onBack = { navController.popBackStack() },
                                onTerminada = { puntaje ->
                                    progreso.completarActividad(3, mejorData?.id ?: 1, puntaje, 25)
                                    versionProgreso++
                                    navController.navigate(NutriRoutes.PREMIO) {
                                        popUpTo(NutriRoutes.ACTIVIDAD_MEJOR) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(NutriRoutes.ACTIVIDAD_RULETA) {
                            val ruletaData = preguntas.actividadesDelNivel(3)
                                .firstOrNull { it.tipo == "ruleta" }
                            RuedaAlimentacionScreen(
                                alimentos = ruletaData?.let { ActividadMapper.ruleta(it) } ?: emptyList(),
                                onBack = { navController.popBackStack() },
                                onTerminada = { puntaje ->
                                    progreso.completarActividad(3, 2, puntaje, 25)
                                    versionProgreso++
                                    navController.navigate(NutriRoutes.PREMIO) {
                                        popUpTo(NutriRoutes.ACTIVIDAD_RULETA) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(NutriRoutes.ACTIVIDAD_MEMORIA) {
                            val memoriaData = preguntas.actividadesDelNivel(1)
                                .firstOrNull { it.tipo == "memoria" }
                            MemoriaNutritivaScreen(
                                pares = memoriaData?.let { ActividadMapper.memoria(it) } ?: emptyList(),
                                onBack = { navController.popBackStack() },
                                onTerminada = { puntaje ->
                                    progreso.completarActividad(1, 3, puntaje, 20)
                                    versionProgreso++
                                    navController.navigate(NutriRoutes.PREMIO) {
                                        popUpTo(NutriRoutes.ACTIVIDAD_MEMORIA) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(NutriRoutes.ACTIVIDAD_DESCUBRE_N2) {
                            DescubreNutrientesScreen(
                                nutrientes = CatalogoNutrientes.TODOS,
                                onBack = { navController.popBackStack() },
                                onTerminada = { puntaje ->
                                    progreso.completarActividad(2, 1, puntaje, 20)
                                    versionProgreso++
                                    navController.navigate(NutriRoutes.PREMIO) {
                                        popUpTo(NutriRoutes.ACTIVIDAD_DESCUBRE_N2) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(NutriRoutes.ACTIVIDAD_VF_N2) {
                            val vfData = preguntas.actividadesDelNivel(2)
                                .firstOrNull { it.tipo == "vf" }
                            VerdaderoFalsoScreen(
                                preguntas = vfData?.let { ActividadMapper.preguntasVF(it) } ?: emptyList(),
                                onBack = { navController.popBackStack() },
                                onTerminada = { puntaje ->
                                    progreso.completarActividad(2, vfData?.id ?: 1, puntaje, 20)
                                    versionProgreso++
                                    navController.navigate(NutriRoutes.PREMIO) {
                                        popUpTo(NutriRoutes.ACTIVIDAD_VF_N2) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(NutriRoutes.ACTIVIDAD_FRASE_N2) {
                            val fraseData = preguntas.actividadesDelNivel(2)
                                .firstOrNull { it.tipo == "completa" }
                            CompletaFraseScreen(
                                frases = fraseData?.let { act ->
                                    ActividadMapper.frases(act).map { frase ->
                                        com.codidevs.nutriapp.ui.actividades.FraseIncompleta(
                                            emoji = frase.emoji,
                                            fraseAntes = frase.antes,
                                            fraseDespues = frase.despues,
                                            respuesta = frase.respuesta,
                                            opciones = frase.opciones
                                        )
                                    }
                                } ?: emptyList(),
                                onBack = { navController.popBackStack() },
                                onTerminada = { puntaje ->
                                    progreso.completarActividad(2, fraseData?.id ?: 2, puntaje, 20)
                                    versionProgreso++
                                    navController.navigate(NutriRoutes.PREMIO) {
                                        popUpTo(NutriRoutes.ACTIVIDAD_FRASE_N2) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(NutriRoutes.ACTIVIDAD_MEJOR_N2) {
                            val mejorData = preguntas.actividadesDelNivel(3)
                                .firstOrNull { it.tipo == "mejor_opcion" }
                            MejorOpcionNivel2Screen(
                                preguntas = mejorData?.let { ActividadMapper.mejorOpcion(it) } ?: emptyList(),
                                onBack = { navController.popBackStack() },
                                onTerminada = { puntaje ->
                                    progreso.completarActividad(3, mejorData?.id ?: 1, puntaje, 25)
                                    versionProgreso++
                                    navController.navigate(NutriRoutes.PREMIO) {
                                        popUpTo(NutriRoutes.ACTIVIDAD_MEJOR_N2) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(NutriRoutes.ACTIVIDAD_QUIZ) {
                            val quizData = preguntas.actividadesDelNivel(4)
                                .firstOrNull { it.tipo == "quiz" }
                            QuizScreen(
                                preguntas = quizData?.let { ActividadMapper.quiz(it) } ?: emptyList(),
                                onBack = { navController.popBackStack() },
                                onTerminada = { puntaje ->
                                    progreso.completarActividad(4, 1, puntaje, 30)
                                    versionProgreso++
                                    navController.navigate(NutriRoutes.PREMIO) {
                                        popUpTo(NutriRoutes.ACTIVIDAD_QUIZ) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(NutriRoutes.ACTIVIDAD_SEMAFORO) {
                            val semaforoData = preguntas.actividadesDelNivel(6)
                                .firstOrNull { it.tipo == "semaforo" }
                            SemaforoScreen(
                                datos = semaforoData?.let { ActividadMapper.semaforo(it) }
                                    ?: SemaforoDatos(emptyList(), emptyList(), emptyList()),
                                onBack = { navController.popBackStack() },
                                onTerminada = { puntaje ->
                                    progreso.completarActividad(6, 2, puntaje, 25)
                                    versionProgreso++
                                    navController.navigate(NutriRoutes.PREMIO) {
                                        popUpTo(NutriRoutes.ACTIVIDAD_SEMAFORO) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(NutriRoutes.ACTIVIDAD_RETO) {
                            val retoData = preguntas.actividadesDelNivel(6)
                                .firstOrNull { it.tipo == "reto" }
                            RetoScreen(
                                acciones = retoData?.let { ActividadMapper.reto(it) } ?: emptyList(),
                                onBack = { navController.popBackStack() },
                                onTerminada = { puntaje ->
                                    progreso.completarActividad(6, 3, puntaje, 25)
                                    versionProgreso++
                                    navController.navigate(NutriRoutes.PREMIO) {
                                        popUpTo(NutriRoutes.ACTIVIDAD_RETO) { inclusive = true }
                                    }
                                }
                            )
                        }
                        // ---- Minijuegos libres (pestaña Juegos): otorgan monedas sin marcar nivel ----
                        composable(NutriRoutes.JUEGO_ARRASTRAR) {
                            GrupoPerteneceScreen(
                                grupos = GruposAlimenticios.TODOS,
                                onBack = { navController.popBackStack() },
                                onTerminada = { puntaje ->
                                    progreso.sumarRecompensa(puntaje, 10)
                                    progreso.completarMinijuego("arrastrar")
                                    versionProgreso++
                                    navController.navigate(NutriRoutes.PREMIO) {
                                        popUpTo(NutriRoutes.JUEGO_ARRASTRAR) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(NutriRoutes.JUEGO_VF) {
                            val vfData = preguntas.actividadesDelNivel(2)
                                .firstOrNull { it.tipo == "vf" }
                            VerdaderoFalsoScreen(
                                preguntas = vfData?.let { ActividadMapper.preguntasVF(it) } ?: emptyList(),
                                onBack = { navController.popBackStack() },
                                onTerminada = { puntaje ->
                                    progreso.sumarRecompensa(puntaje, 10)
                                    progreso.completarMinijuego("vf")
                                    versionProgreso++
                                    navController.navigate(NutriRoutes.PREMIO) {
                                        popUpTo(NutriRoutes.JUEGO_VF) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(NutriRoutes.JUEGO_COMPLETA) {
                            val fraseData = preguntas.actividadesDelNivel(2)
                                .firstOrNull { it.tipo == "completa" }
                            CompletaFraseScreen(
                                frases = fraseData?.let { act ->
                                    ActividadMapper.frases(act).map { frase ->
                                        com.codidevs.nutriapp.ui.actividades.FraseIncompleta(
                                            emoji = frase.emoji,
                                            fraseAntes = frase.antes,
                                            fraseDespues = frase.despues,
                                            respuesta = frase.respuesta,
                                            opciones = frase.opciones
                                        )
                                    }
                                } ?: emptyList(),
                                onBack = { navController.popBackStack() },
                                onTerminada = { puntaje ->
                                    progreso.sumarRecompensa(puntaje, 10)
                                    progreso.completarMinijuego("completa")
                                    versionProgreso++
                                    navController.navigate(NutriRoutes.PREMIO) {
                                        popUpTo(NutriRoutes.JUEGO_COMPLETA) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(NutriRoutes.JUEGO_MEJOR) {
                            val mejorData = preguntas.actividadesDelNivel(3)
                                .firstOrNull { it.tipo == "mejor_opcion" }
                            MejorOpcionNivel2Screen(
                                preguntas = mejorData?.let { ActividadMapper.mejorOpcion(it) } ?: emptyList(),
                                onBack = { navController.popBackStack() },
                                onTerminada = { puntaje ->
                                    progreso.sumarRecompensa(puntaje, 10)
                                    progreso.completarMinijuego("mejor")
                                    versionProgreso++
                                    navController.navigate(NutriRoutes.PREMIO) {
                                        popUpTo(NutriRoutes.JUEGO_MEJOR) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(NutriRoutes.JUEGO_RULETA) {
                            val ruletaData = preguntas.actividadesDelNivel(3)
                                .firstOrNull { it.tipo == "ruleta" }
                            RuedaAlimentacionScreen(
                                alimentos = ruletaData?.let { ActividadMapper.ruleta(it) } ?: emptyList(),
                                onBack = { navController.popBackStack() },
                                onTerminada = { puntaje ->
                                    progreso.sumarRecompensa(puntaje, 10)
                                    progreso.completarMinijuego("ruleta")
                                    versionProgreso++
                                    navController.navigate(NutriRoutes.PREMIO) {
                                        popUpTo(NutriRoutes.JUEGO_RULETA) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(NutriRoutes.JUEGO_MEMORIA) {
                            val memoriaData = preguntas.actividadesDelNivel(1)
                                .firstOrNull { it.tipo == "memoria" }
                            MemoriaNutritivaScreen(
                                pares = memoriaData?.let { ActividadMapper.memoria(it) } ?: emptyList(),
                                onBack = { navController.popBackStack() },
                                onTerminada = { puntaje ->
                                    progreso.sumarRecompensa(puntaje, 10)
                                    progreso.completarMinijuego("memoria")
                                    versionProgreso++
                                    navController.navigate(NutriRoutes.PREMIO) {
                                        popUpTo(NutriRoutes.JUEGO_MEMORIA) { inclusive = true }
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
                            val nutricionCompleto = progreso.nivelCompleto(1, preguntas.totalActividadesNivel(1)) &&
                                progreso.nivelCompleto(2, preguntas.totalActividadesNivel(2)) &&
                                progreso.nivelCompleto(3, preguntas.totalActividadesNivel(3))
                            val actividadCompleto = progreso.nivelCompleto(4, preguntas.totalActividadesNivel(4)) &&
                                progreso.nivelCompleto(5, preguntas.totalActividadesNivel(5)) &&
                                progreso.nivelCompleto(6, preguntas.totalActividadesNivel(6)) &&
                                progreso.nivelCompleto(7, preguntas.totalActividadesNivel(7))
                            val todosNiveles = (1..7).all { progreso.nivelCompleto(it, preguntas.totalActividadesNivel(it)) }
                            val minijuegosCompleto = listOf("arrastrar", "vf", "completa", "mejor", "ruleta", "memoria")
                                .all { progreso.minijuegoCompletado(it) }
                            val medallas = CatalogoMedallas.conProgreso(
                                nutricionCompleto, actividadCompleto, todosNiveles, minijuegosCompleto
                            )
                            RecompensasScreen(
                                monedas = progreso.monedasTotal,
                                medallas = medallas,
                                canjeadas = remember(versionProgreso) {
                                    medallas.filter { progreso.recompensaCanjeada(it.id) }.map { it.id }.toSet()
                                },
                                onCanjear = { medalla ->
                                    val costo = if (medalla.especial) 500 else 200
                                    progreso.canjearRecompensa(medalla.id, costo)
                                    versionProgreso++
                                },
                                onCerrar = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}

/** Calcula el texto "Nivel X · Módulo" que se muestra en el Home. */
private fun nivelActualTexto(
    progreso: com.codidevs.nutriapp.data.repository.ProgresoRepository,
    preguntas: com.codidevs.nutriapp.data.repository.PreguntasRepository,
    modulo: Int
): String {
    val nombreModulo = if (modulo == 1) "Nutrición" else "Actividad física"
    // Nivel actual: primero sin completar del módulo
    val rango = if (modulo == 1) 1..3 else 4..7
    val nivelActual = rango.firstOrNull { nivel ->
        !progreso.nivelCompleto(nivel, preguntas.totalActividadesNivel(nivel))
    } ?: rango.last
    return "Nivel $nivelActual · $nombreModulo"
}

/** Nivel global actual (1-7): el primero sin completar. */
private fun nivelActualGlobal(
    progreso: com.codidevs.nutriapp.data.repository.ProgresoRepository,
    preguntas: com.codidevs.nutriapp.data.repository.PreguntasRepository
): Int {
    val nivel = (1..7).firstOrNull { nivel ->
        !progreso.nivelCompleto(nivel, preguntas.totalActividadesNivel(nivel))
    } ?: 7
    return nivel
}
