package com.example.proyecto_final.ui.common

/**
 * Estado genérico de una pantalla. Lo reutilizan todas las pantallas para
 * representar carga, éxito y error (incluidos errores de conexión).
 */
sealed interface EstadoUi<out T> {
    data object Cargando : EstadoUi<Nothing>
    data class Exito<T>(val datos: T) : EstadoUi<T>
    data class Error(val mensaje: String) : EstadoUi<Nothing>
}
