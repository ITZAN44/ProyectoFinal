package com.example.proyecto_final.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyecto_final.ui.home.PantallaPrincipal
import com.example.proyecto_final.ui.login.PantallaInicioSesion

/** Grafo de navegación de la app. */
@Composable
fun NavegacionApp(rutaInicial: String) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = rutaInicial) {
        composable(Pantalla.InicioSesion.ruta) {
            PantallaInicioSesion(
                onIngresar = {
                    navController.navigate(Pantalla.Principal.ruta) {
                        popUpTo(Pantalla.InicioSesion.ruta) { inclusive = true }
                    }
                }
            )
        }
        composable(Pantalla.Principal.ruta) {
            PantallaPrincipal()
        }
    }
}
