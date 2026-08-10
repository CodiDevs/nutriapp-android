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
import com.codidevs.nutriapp.data.models.CatalogoMedallas
import com.codidevs.nutriapp.data.models.GruposAlimenticios
import com.codidevs.nutriapp.data.repository.ActividadMapper
import com.codidevs.nutriapp.data.repository.PreguntasRepository
import com.codidevs.nutriapp.data.repository.ProgresoRepository
import com.codidevs.nutriapp.ui.actividades.ActividadGenericaScreen
import com.codidevs.nutriapp.ui.actividades.CompletaFraseScreen
import com.codidevs.nutriapp.ui.actividades.GrupoPerteneceScreen
import com.codidevs.nutriapp.ui.actividades.MejorOpcionNivel2Screen
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
import com.codidevs.nutriapp.ui.recompensas.RecompensasScreen
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
                    // Registra el día activo para la racha (solo una vez por apertura)
                    remember { progreso.registrarDiaActivo() }
                    // Si ya hay un usuario registrado, arranca directo en el Home
                    val inicio = if (progreso.usuarioRegistrado) NutriRoutes.HOME else NutriRoutes.SPLASH
                    var nombreUsuario by remember { mutableStateOf(progreso.usuarioNombre) }
                    var tabActiva by remember { mutableStateOf("home") }
                    var moduloActual by remember { mutableStateOf(1) } // 1 = Nutrición, 2 = Actividad física
                    // Refresca la UI cuando cambia el progreso
                    var versionProgreso by remember { mutableStateOf(0) }
                    // Mapas de niveles para calcular los totales (actividades y monedas por nivel)
                    val actividadesPorNivel = remember {
                        (1..7).associateWith { preguntas.totalActividadesNivel(it) }
                    }
                    val monedasPorNivel = remember {
                        (1..7).associateWith { preguntas.nivel(it)?.monedas ?: 20 }
                    }

                    NavHost(
                        navController = navController,
                        startDestination = inicio,
                        modifier = Modifier
                            .fillMaxSize()
                            .safeDrawingPadding()
                    ) {
                        composable(NutriRoutes.SPLASH) {
                            SplashScreen(onComenzar = {
                                navController.navigate(
                                    if (progreso.usuarioRegistrado) NutriRoutes.HOME else NutriRoutes.REGISTRO
                                )
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
                                    // Guarda el usuario registrado (persistente)
                                    progreso.guardarUsuario(nombre, edad, peso, estatura)
                                    nombreUsuario = nombre
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
                            val totalMonedas = remember(versionProgreso) {
                                progreso.monedasTotales(monedasPorNivel, actividadesPorNivel)
                            }
                            val totalEstrellas = remember(versionProgreso) {
                                progreso.estrellasTotales(actividadesPorNivel)
                            }
                            TabScaffold(
                                tabActiva = tabActiva,
                                onTab = { tabActiva = it },
                                monedas = "🪙 $totalMonedas",
                                racha = "🔥 ${progreso.rachaDias}",
                                estrellas = "⭐ $totalEstrellas"
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
                                        estrellasNivel = remember(versionProgreso) {
                                            (1..7).associateWith { nivel ->
                                                // Total de estrellas asignadas del nivel (suma 3 si está completo)
                                                val acts = preguntas.actividadesDelNivel(nivel)
                                                acts.sumOf {
                                                    progreso.estrellasAsignadasActividad(nivel, it.id, acts.size)
                                                }.coerceIn(0, 3)
                                            }
                                        },
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
                                        estrellas = remember(versionProgreso) {
                                            listOf("arrastrar", "vf", "completa", "mejor", "ruleta", "memoria")
                                                .associateWith { progreso.estrellasMinijuego(it) }
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
                                            },
                                            onCrearRegistro = {
                                                // Borra usuario y progreso, y reinicia el registro
                                                progreso.borrarTodo()
                                                nombreUsuario = ""
                                                versionProgreso++
                                                navController.navigate(NutriRoutes.REGISTRO) {
                                                    popUpTo(NutriRoutes.HOME) { inclusive = true }
                                                }
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
                                    // Total de monedas del nivel: monedas por actividad × número de actividades
                                    monedas = "+${n.monedas * n.actividades.size}"
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
                            // Estrellas asignadas de cada actividad (1-3 según nivel, 0 si no completada)
                            val estrellasActividades = remember(nivelId, versionProgreso) {
                                actividadesNivel.associate { act ->
                                    act.id to progreso.estrellasAsignadasActividad(
                                        nivelId, act.id, actividadesNivel.size
                                    )
                                }
                            }
                            ActividadesScreen(
                                nivelNumero = nivelId,
                                actividades = actividadesNivel,
                                estrellas = estrellasActividades,
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
                                totalPreguntas = when (actJson?.tipo) {
                                    "reto" -> 10 // reto tiene 10 acciones
                                    else -> (datos as? List<*>)?.size ?: 0
                                },
                                onBack = { navController.popBackStack() },
                                onTerminada = { puntaje, porcentaje ->
                                    val monedasNivel = preguntas.nivel(nivelId)?.monedas ?: 20
                                    val totalMonedasAntes = progreso.monedasTotales(monedasPorNivel, actividadesPorNivel)
                                    val estrellasGanadas = progreso.registrarResultadoActividad(
                                        nivelId, actividadId, porcentaje, monedasNivel
                                    )
                                    val totalMonedasDespues = progreso.monedasTotales(monedasPorNivel, actividadesPorNivel)
                                    // El premio muestra SOLO lo que realmente se añadió al total (la diferencia)
                                    val monedasGanadas = (totalMonedasDespues - totalMonedasAntes).coerceAtLeast(0)
                                    // Estrellas asignadas de la actividad (según nivel y posición)
                                    val totalAct = actividadesPorNivel[nivelId] ?: 1
                                    val estrellasPremio = progreso.estrellasAsignadasActividad(
                                        nivelId, actividadId, totalAct
                                    )
                                    versionProgreso++
                                    navController.navigate(
                                        "${NutriRoutes.PREMIO}/$porcentaje/$estrellasPremio/$monedasGanadas"
                                    ) {
                                        popUpTo("${NutriRoutes.ACTIVIDAD}/$nivelId/$actividadId") { inclusive = true }
                                    }
                                }
                            )
                        }
                        // ---- Minijuegos libres (pestaña Juegos) ----
                        // Recompensan solo si se supera el mejor desempeño anterior (anti-farmeo).
                        // El premio muestra lo que realmente se sumó al total (la diferencia).
                        composable(NutriRoutes.JUEGO_ARRASTRAR) {
                            GrupoPerteneceScreen(
                                grupos = GruposAlimenticios.TODOS,
                                onBack = { navController.popBackStack() },
                                onTerminada = { puntaje ->
                                    val porcentaje = (puntaje * 100 / 60).coerceIn(0, 100) // 6 rondas x 10
                                    val estrellas = progreso.estrellasPorPorcentaje(porcentaje)
                                    val totalAntes = progreso.monedasTotales(monedasPorNivel, actividadesPorNivel)
                                    if (estrellas > progreso.estrellasMinijuego("arrastrar")) {
                                        progreso.setEstrellasMinijuego("arrastrar", estrellas)
                                    }
                                    val monedasGanadas = (progreso.monedasTotales(
                                        monedasPorNivel, actividadesPorNivel
                                    ) - totalAntes).coerceAtLeast(0)
                                    versionProgreso++
                                    navController.navigate(
                                        "${NutriRoutes.PREMIO}/$porcentaje/$estrellas/$monedasGanadas"
                                    ) {
                                        popUpTo(NutriRoutes.JUEGO_ARRASTRAR) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(NutriRoutes.JUEGO_VF) {
                            // Mezcla V/F de alimentos (nivel 2) y deporte (nivel 5), fijado con remember
                            val vfAlimentos = preguntas.actividadesDelNivel(2)
                                .firstOrNull { it.tipo == "vf" }
                            val vfDeporte = preguntas.actividadesDelNivel(5)
                                .firstOrNull { it.tipo == "vf" }
                            val preguntasVF = remember {
                                ((vfAlimentos?.let { ActividadMapper.preguntasVF(it) } ?: emptyList()) +
                                    (vfDeporte?.let { ActividadMapper.preguntasVF(it) } ?: emptyList())).shuffled()
                            }
                            VerdaderoFalsoScreen(
                                preguntas = preguntasVF,
                                onBack = { navController.popBackStack() },
                                onTerminada = { puntaje ->
                                    val maximo = preguntasVF.size * 10
                                    val porcentaje = if (maximo > 0) (puntaje * 100 / maximo) else 0
                                    val estrellas = progreso.estrellasPorPorcentaje(porcentaje)
                                    val totalAntes = progreso.monedasTotales(monedasPorNivel, actividadesPorNivel)
                                    if (estrellas > progreso.estrellasMinijuego("vf")) {
                                        progreso.setEstrellasMinijuego("vf", estrellas)
                                    }
                                    val monedasGanadas = (progreso.monedasTotales(
                                        monedasPorNivel, actividadesPorNivel
                                    ) - totalAntes).coerceAtLeast(0)
                                    versionProgreso++
                                    navController.navigate(
                                        "${NutriRoutes.PREMIO}/$porcentaje/$estrellas/$monedasGanadas"
                                    ) {
                                        popUpTo(NutriRoutes.JUEGO_VF) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(NutriRoutes.JUEGO_COMPLETA) {
                            // Mezcla frases de alimentos (nivel 2) y hábitos/deporte (nivel 7), fijado con remember
                            val fraseAlimentos = preguntas.actividadesDelNivel(2)
                                .firstOrNull { it.tipo == "completa" }
                            val fraseHabitos = preguntas.actividadesDelNivel(7)
                                .firstOrNull { it.tipo == "completa" }
                            val frases = remember {
                                ((fraseAlimentos?.let { ActividadMapper.frases(it) } ?: emptyList()) +
                                    (fraseHabitos?.let { ActividadMapper.frases(it) } ?: emptyList()))
                                    .map { frase ->
                                        com.codidevs.nutriapp.ui.actividades.FraseIncompleta(
                                            emoji = frase.emoji,
                                            fraseAntes = frase.antes,
                                            fraseDespues = frase.despues,
                                            respuesta = frase.respuesta,
                                            opciones = frase.opciones
                                        )
                                    }.shuffled()
                            }
                            CompletaFraseScreen(
                                frases = frases,
                                onBack = { navController.popBackStack() },
                                onTerminada = { puntaje ->
                                    val maximo = frases.size * 10
                                    val porcentaje = if (maximo > 0) (puntaje * 100 / maximo) else 0
                                    val estrellas = progreso.estrellasPorPorcentaje(porcentaje)
                                    val totalAntes = progreso.monedasTotales(monedasPorNivel, actividadesPorNivel)
                                    if (estrellas > progreso.estrellasMinijuego("completa")) {
                                        progreso.setEstrellasMinijuego("completa", estrellas)
                                    }
                                    val monedasGanadas = (progreso.monedasTotales(
                                        monedasPorNivel, actividadesPorNivel
                                    ) - totalAntes).coerceAtLeast(0)
                                    versionProgreso++
                                    navController.navigate(
                                        "${NutriRoutes.PREMIO}/$porcentaje/$estrellas/$monedasGanadas"
                                    ) {
                                        popUpTo(NutriRoutes.JUEGO_COMPLETA) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(NutriRoutes.JUEGO_MEJOR) {
                            // Mezcla mejor opción de alimentos (nivel 3), plato saludable (4) y hábitos (6), fijado con remember
                            val mejorAlimentos = preguntas.actividadesDelNivel(3)
                                .firstOrNull { it.tipo == "mejor_opcion" }
                            val mejorDeporte1 = preguntas.actividadesDelNivel(4)
                                .firstOrNull { it.tipo == "mejor_opcion" }
                            val mejorDeporte2 = preguntas.actividadesDelNivel(6)
                                .firstOrNull { it.tipo == "mejor_opcion" }
                            val preguntasMejor = remember {
                                ((mejorAlimentos?.let { ActividadMapper.mejorOpcion(it) } ?: emptyList()) +
                                    (mejorDeporte1?.let { ActividadMapper.mejorOpcion(it) } ?: emptyList()) +
                                    (mejorDeporte2?.let { ActividadMapper.mejorOpcion(it) } ?: emptyList())).shuffled()
                            }
                            MejorOpcionNivel2Screen(
                                preguntas = preguntasMejor,
                                onBack = { navController.popBackStack() },
                                onTerminada = { puntaje ->
                                    val maximo = preguntasMejor.size * 10
                                    val porcentaje = if (maximo > 0) (puntaje * 100 / maximo) else 0
                                    val estrellas = progreso.estrellasPorPorcentaje(porcentaje)
                                    val totalAntes = progreso.monedasTotales(monedasPorNivel, actividadesPorNivel)
                                    if (estrellas > progreso.estrellasMinijuego("mejor")) {
                                        progreso.setEstrellasMinijuego("mejor", estrellas)
                                    }
                                    val monedasGanadas = (progreso.monedasTotales(
                                        monedasPorNivel, actividadesPorNivel
                                    ) - totalAntes).coerceAtLeast(0)
                                    versionProgreso++
                                    navController.navigate(
                                        "${NutriRoutes.PREMIO}/$porcentaje/$estrellas/$monedasGanadas"
                                    ) {
                                        popUpTo(NutriRoutes.JUEGO_MEJOR) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(NutriRoutes.JUEGO_RULETA) {
                            val ruletaData = preguntas.actividadesDelNivel(3)
                                .firstOrNull { it.tipo == "ruleta" }
                            val alimentosRuleta = remember {
                                ruletaData?.let { ActividadMapper.ruleta(it) } ?: emptyList()
                            }
                            RuedaAlimentacionScreen(
                                alimentos = alimentosRuleta,
                                onBack = { navController.popBackStack() },
                                onTerminada = { puntaje ->
                                    val maximo = alimentosRuleta.size * 10
                                    val porcentaje = if (maximo > 0) (puntaje * 100 / maximo) else 0
                                    val estrellas = progreso.estrellasPorPorcentaje(porcentaje)
                                    val totalAntes = progreso.monedasTotales(monedasPorNivel, actividadesPorNivel)
                                    if (estrellas > progreso.estrellasMinijuego("ruleta")) {
                                        progreso.setEstrellasMinijuego("ruleta", estrellas)
                                    }
                                    val monedasGanadas = (progreso.monedasTotales(
                                        monedasPorNivel, actividadesPorNivel
                                    ) - totalAntes).coerceAtLeast(0)
                                    versionProgreso++
                                    navController.navigate(
                                        "${NutriRoutes.PREMIO}/$porcentaje/$estrellas/$monedasGanadas"
                                    ) {
                                        popUpTo(NutriRoutes.JUEGO_RULETA) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(NutriRoutes.JUEGO_MEMORIA) {
                            // Mezcla memoria de alimentos (nivel 1) con deporte (nivel 7),
                            // limitada a 8 parejas para que no estrese, fijada con remember
                            val memoriaAlimentos = preguntas.actividadesDelNivel(1)
                                .firstOrNull { it.tipo == "memoria" }
                            val memoriaDeporte = preguntas.actividadesDelNivel(7)
                                .firstOrNull { it.tipo == "une" }
                            val pares = remember {
                                val todas = (memoriaAlimentos?.let { ActividadMapper.memoria(it) } ?: emptyList()) +
                                    (memoriaDeporte?.let { ActividadMapper.une(it).map { u ->
                                        com.codidevs.nutriapp.ui.actividades.ParMemoria(u.emoji, u.texto)
                                    } } ?: emptyList())
                                todas.shuffled().take(8)
                            }
                            MemoriaNutritivaScreen(
                                pares = pares,
                                onBack = { navController.popBackStack() },
                                onTerminada = { puntaje ->
                                    val maximo = pares.size * 10
                                    val porcentaje = if (maximo > 0) (puntaje * 100 / maximo) else 0
                                    val estrellas = progreso.estrellasPorPorcentaje(porcentaje)
                                    val totalAntes = progreso.monedasTotales(monedasPorNivel, actividadesPorNivel)
                                    if (estrellas > progreso.estrellasMinijuego("memoria")) {
                                        progreso.setEstrellasMinijuego("memoria", estrellas)
                                    }
                                    val monedasGanadas = (progreso.monedasTotales(
                                        monedasPorNivel, actividadesPorNivel
                                    ) - totalAntes).coerceAtLeast(0)
                                    versionProgreso++
                                    navController.navigate(
                                        "${NutriRoutes.PREMIO}/$porcentaje/$estrellas/$monedasGanadas"
                                    ) {
                                        popUpTo(NutriRoutes.JUEGO_MEMORIA) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(
                            "${NutriRoutes.PREMIO}/{porcentaje}/{estrellas}/{monedas}"
                        ) { backStackEntry ->
                            val args = backStackEntry.arguments
                            PremioScreen(
                                porcentaje = args?.getString("porcentaje")?.toIntOrNull() ?: 100,
                                estrellas = args?.getString("estrellas")?.toIntOrNull() ?: 0,
                                monedas = args?.getString("monedas")?.toIntOrNull() ?: 0,
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
                                monedas = progreso.monedasTotales(monedasPorNivel, actividadesPorNivel),
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
