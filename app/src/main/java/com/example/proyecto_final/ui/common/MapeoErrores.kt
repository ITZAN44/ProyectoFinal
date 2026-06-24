package com.example.proyecto_final.ui.common

import retrofit2.HttpException
import java.io.IOException

/** Traduce una excepción de red a un mensaje legible para el usuario. */
fun mensajeDeError(error: Throwable): String = when (error) {
    is IOException -> "Sin conexión. Verificá tu internet."
    is HttpException -> when (error.code()) {
        401, 422 -> "Correo o contraseña incorrectos."
        in 500..599 -> "Error del servidor. Probá más tarde."
        else -> "No se pudo completar la operación."
    }
    else -> "Ocurrió un error inesperado."
}
