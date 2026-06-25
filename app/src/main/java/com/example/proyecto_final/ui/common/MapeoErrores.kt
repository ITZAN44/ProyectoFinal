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

/** Mensajes específicos al unirse a un grupo (el genérico no cubre 409/404). */
fun mensajeErrorUnirseGrupo(error: Throwable): String = when {
    error is HttpException && error.code() == 409 -> "Ya sos miembro de este grupo."
    error is HttpException && error.code() == 404 -> "Código de invitación inválido."
    else -> mensajeDeError(error)
}

/** Mensajes específicos al pronosticar. El 422 acá es "partido ya iniciado", NO credenciales. */
fun mensajeErrorPronostico(error: Throwable): String = when {
    error is HttpException && error.code() == 422 -> "No se puede pronosticar: el partido ya comenzó o finalizó."
    else -> mensajeDeError(error)
}
