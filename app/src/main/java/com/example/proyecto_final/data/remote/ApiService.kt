package com.example.proyecto_final.data.remote

import com.example.proyecto_final.data.remote.dto.LoginRequest
import com.example.proyecto_final.data.remote.dto.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Definición de los endpoints de la API de Quiniela 2026.
 * Base URL: http://quiniela.jmacboy.com/  ·  Los endpoints se agregan por fase.
 */
interface ApiService {

    /** Health check del servidor (sin prefijo /api, sin auth). */
    @GET("up")
    suspend fun verificarEstado(): Response<Unit>

    /** Inicia sesión y devuelve el token + datos básicos del usuario. */
    @POST("api/login")
    suspend fun login(@Body solicitud: LoginRequest): LoginResponse
}
