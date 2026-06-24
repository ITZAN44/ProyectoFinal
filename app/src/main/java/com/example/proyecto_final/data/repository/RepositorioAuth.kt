package com.example.proyecto_final.data.repository

import com.example.proyecto_final.data.local.GestorSesion
import com.example.proyecto_final.data.remote.ApiService
import com.example.proyecto_final.data.remote.dto.LoginRequest
import javax.inject.Inject

/** Repositorio de autenticación: login contra la API y persistencia de la sesión. */
class RepositorioAuth @Inject constructor(
    private val apiService: ApiService,
    private val gestorSesion: GestorSesion
) {

    /** Inicia sesión y guarda el token + datos del usuario. */
    suspend fun iniciarSesion(email: String, contrasena: String): Result<Unit> = runCatching {
        val respuesta = apiService.login(LoginRequest(email = email, password = contrasena))
        gestorSesion.guardarSesion(
            token = respuesta.token,
            nombre = respuesta.name,
            email = respuesta.email
        )
    }
}
