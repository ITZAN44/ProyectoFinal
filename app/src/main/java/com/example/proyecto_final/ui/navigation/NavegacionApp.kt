package com.example.proyecto_final.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyecto_final.ui.MainViewModel
import com.example.proyecto_final.ui.login.PantallaInicioSesion

/** Grafo de navegación raíz: autenticación y, ya logueado, el menú principal. */
@Composable
fun NavegacionApp(rutaInicial: String, viewModel: MainViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val rutaSesion by viewModel.rutaInicial.collectAsState()

    // Único redirect al login: si la sesión se invalida estando dentro del menú (logout manual
    // o token revocado/expirado → 401), volvemos al login y limpiamos el back stack.
    LaunchedEffect(rutaSesion) {
        if (rutaSesion == Pantalla.InicioSesion.ruta &&
            navController.currentDestination?.route == Pantalla.Menu.ruta
        ) {
            navController.navigate(Pantalla.InicioSesion.ruta) {
                popUpTo(Pantalla.Menu.ruta) { inclusive = true }
            }
        }
    }

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
            MenuPrincipal()
        }
    }
}
