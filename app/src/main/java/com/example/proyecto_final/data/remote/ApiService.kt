package com.example.proyecto_final.data.remote

import com.example.proyecto_final.data.remote.dto.GrupoDto
import com.example.proyecto_final.data.remote.dto.LoginRequest
import com.example.proyecto_final.data.remote.dto.LoginResponse
import com.example.proyecto_final.data.remote.dto.PartidoDto
import com.example.proyecto_final.data.remote.dto.PerfilDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

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

    /** Revoca el token actual del usuario autenticado. */
    @POST("api/logout")
    suspend fun cerrarSesion(): Response<Unit>

    /** Perfil y estadísticas del usuario autenticado. */
    @GET("api/profile")
    suspend fun obtenerPerfil(): PerfilDto

    /** Grupos del usuario con su puntaje en cada uno. */
    @GET("api/groups")
    suspend fun obtenerGrupos(): List<GrupoDto>

    /** Próximos partidos programados (con next=true devuelve los próximos 10). */
    @GET("api/matches")
    suspend fun obtenerProximosPartidos(@Query("next") proximos: Boolean = true): List<PartidoDto>
}
