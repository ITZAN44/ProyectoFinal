package com.example.proyecto_final.data.repository

import com.example.proyecto_final.data.local.GestorSesion
import com.example.proyecto_final.data.local.dao.GrupoDao
import com.example.proyecto_final.data.local.dao.PerfilDao
import com.example.proyecto_final.data.remote.ApiService
import com.example.proyecto_final.data.remote.dto.LoginRequest
import javax.inject.Inject

/** Repositorio de autenticación: login, logout y persistencia de la sesión. */
class RepositorioAuth @Inject constructor(
    private val apiService: ApiService,
    private val gestorSesion: GestorSesion,
    private val perfilDao: PerfilDao,
    private val grupoDao: GrupoDao
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

    /**
     * Cierra sesión: intenta revocar el token en el server y SIEMPRE limpia lo local
     * (sesión + datos del usuario en Room), aunque no haya conexión.
     */
    suspend fun cerrarSesion() {
        runCatching { apiService.cerrarSesion() }
        gestorSesion.limpiarSesion()
        perfilDao.borrar()
        grupoDao.borrarTodos()
    }
}
