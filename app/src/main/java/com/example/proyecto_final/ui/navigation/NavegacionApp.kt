package com.example.proyecto_final.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyecto_final.ui.login.PantallaInicioSesion

/** Grafo de navegación raíz: autenticación y, ya logueado, el menú principal. */
@Composable
fun NavegacionApp(rutaInicial: String) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = rutaInicial) {
        composable(Pantalla.InicioSesion.ruta) {
            PantallaInicioSesion(
                onIngresar = {
                    navController.navigate(Pantalla.Menu.ruta) {
                        popUpTo(Pantalla.InicioSesion.ruta) { inclusive = true }
                    }
                }
            )
        }
        composable(Pantalla.Menu.ruta) {
            MenuPrincipal(
                alCerrarSesion = {
                    navController.navigate(Pantalla.InicioSesion.ruta) {
                        popUpTo(Pantalla.Menu.ruta) { inclusive = true }
                    }
                }
            )
        }
    }
}
