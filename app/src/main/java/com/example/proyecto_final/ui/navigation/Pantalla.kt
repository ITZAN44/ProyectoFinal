package com.example.proyecto_final.ui.navigation

/** Rutas de navegación de la app. Se irán agregando a medida que avancen las fases. */
sealed class Pantalla(val ruta: String) {
    data object InicioSesion : Pantalla("inicio_sesion")
    data object Menu : Pantalla("menu")
}
