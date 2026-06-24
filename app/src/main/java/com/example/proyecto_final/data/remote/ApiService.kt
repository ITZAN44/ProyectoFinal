package com.example.proyecto_final.data.remote

import retrofit2.Response
import retrofit2.http.GET

/**
 * Definición de los endpoints de la API de Quiniela 2026.
 * Base URL: http://quiniela.jmacboy.com/  ·  Los endpoints se agregan por fase.
 */
interface ApiService {

    /** Health check del servidor (sin prefijo /api, sin auth). */
    @GET("up")
    suspend fun verificarEstado(): Response<Unit>
}
