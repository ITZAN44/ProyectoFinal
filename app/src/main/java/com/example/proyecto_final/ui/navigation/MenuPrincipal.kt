package com.example.proyecto_final.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyecto_final.ui.groups.PantallaDetalleGrupo
import com.example.proyecto_final.ui.groups.PantallaMisGrupos
import com.example.proyecto_final.ui.home.PantallaPrincipal
import com.example.proyecto_final.ui.matches.PantallaDetallePartido
import com.example.proyecto_final.ui.matches.PantallaPartidos
import com.example.proyecto_final.ui.profile.PantallaPerfil
import com.example.proyecto_final.ui.stadiums.PantallaMapaSedes

/** Secciones del menú principal. Por ahora solo "Inicio" está implementada. */
private enum class SeccionMenu(val titulo: String, val icono: ImageVector) {
    Inicio("Inicio", Icons.Default.Home),
    Grupos("Grupos", Icons.Default.Person),
    Partidos("Partidos", Icons.Default.DateRange),
    Mapa("Mapa", Icons.Default.Place),
    Perfil("Perfil", Icons.Default.AccountCircle)
}

/** Shell con barra de navegación inferior que aloja las pantallas principales. */
@Composable
fun MenuPrincipal(alCerrarSesion: () -> Unit) {
    var seccion by rememberSaveable { mutableStateOf(SeccionMenu.Inicio) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                SeccionMenu.entries.forEach { item ->
                    NavigationBarItem(
                        selected = seccion == item,
                        onClick = { seccion = item },
                        icon = { Icon(item.icono, contentDescription = item.titulo) },
                        label = { Text(item.titulo) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (seccion) {
                SeccionMenu.Inicio -> PantallaPrincipal()
                SeccionMenu.Grupos -> NavegacionGrupos()
                SeccionMenu.Partidos -> NavegacionPartidos()
                SeccionMenu.Mapa -> PantallaMapaSedes()
                SeccionMenu.Perfil -> PantallaPerfil(onSesionCerrada = alCerrarSesion)
            }
        }
    }
}

/** Navegación interna de la sección Grupos: lista → detalle del grupo → detalle del partido. */
@Composable
private fun NavegacionGrupos() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "lista") {
        composable("lista") {
            PantallaMisGrupos(
                alAbrirGrupo = { grupoId -> navController.navigate("detalle/$grupoId") }
            )
        }
        composable(
            route = "detalle/{grupoId}",
            arguments = listOf(navArgument("grupoId") { type = NavType.IntType })
        ) {
            PantallaDetalleGrupo(
                alVolver = { navController.popBackStack() },
                alAbrirPartido = { matchId -> navController.navigate("partido/$matchId") }
            )
        }
        composable(
            route = "partido/{matchId}",
            arguments = listOf(navArgument("matchId") { type = NavType.IntType })
        ) {
            PantallaDetallePartido(alVolver = { navController.popBackStack() })
        }
    }
}

/** Navegación interna de la sección Partidos: lista → detalle del partido. */
@Composable
private fun NavegacionPartidos() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "lista") {
        composable("lista") {
            PantallaPartidos(
                alAbrirPartido = { matchId -> navController.navigate("detalle/$matchId") }
            )
        }
        composable(
            route = "detalle/{matchId}",
            arguments = listOf(navArgument("matchId") { type = NavType.IntType })
        ) {
            PantallaDetallePartido(alVolver = { navController.popBackStack() })
        }
    }
}
