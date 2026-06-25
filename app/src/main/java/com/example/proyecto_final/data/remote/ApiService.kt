package com.example.proyecto_final.data.remote

import com.example.proyecto_final.data.remote.dto.ActualizacionPartidosDto
import com.example.proyecto_final.data.remote.dto.CrearGrupoRequest
import com.example.proyecto_final.data.remote.dto.DetalleGrupoDto
import com.example.proyecto_final.data.remote.dto.GrupoCreadoDto
import com.example.proyecto_final.data.remote.dto.GrupoDto
import com.example.proyecto_final.data.remote.dto.LeaderboardEntradaDto
import com.example.proyecto_final.data.remote.dto.LoginRequest
import com.example.proyecto_final.data.remote.dto.LoginResponse
import com.example.proyecto_final.data.remote.dto.PartidoDto
import com.example.proyecto_final.data.remote.dto.PerfilDto
import com.example.proyecto_final.data.remote.dto.UnirseGrupoDto
import com.example.proyecto_final.data.remote.dto.UnirseGrupoRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
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

    /** Crea un grupo nuevo. Devuelve su código de invitación. */
    @POST("api/groups")
    suspend fun crearGrupo(@Body solicitud: CrearGrupoRequest): GrupoCreadoDto

    /** Se une a un grupo por código. 409 si ya es miembro, 404 si el código es inválido. */
    @POST("api/groups/join")
    suspend fun unirseAGrupo(@Body solicitud: UnirseGrupoRequest): UnirseGrupoDto

    /** Detalle de un grupo (incluye los próximos partidos). 403 si no es miembro. */
    @GET("api/groups/{id}")
    suspend fun obtenerDetalleGrupo(@Path("id") id: Int): DetalleGrupoDto

    /** Clasificación de un grupo, ordenada por puntaje. 403 si no es miembro. */
    @GET("api/groups/{id}/leaderboard")
    suspend fun obtenerClasificacion(@Path("id") id: Int): List<LeaderboardEntradaDto>

    /** Próximos partidos programados (con next=true devuelve los próximos 10). */
    @GET("api/matches")
    suspend fun obtenerProximosPartidos(@Query("next") proximos: Boolean = true): List<PartidoDto>

    /** Calendario completo. Los filtros nulos no se envían (Retrofit los omite). */
    @GET("api/matches")
    suspend fun obtenerPartidos(
        @Query("phase") fase: String? = null,
        @Query("status") estado: String? = null,
        @Query("date") fecha: String? = null
    ): List<PartidoDto>

    /** Partidos modificados desde `since` (ISO8601). Devuelve { synced_at, games }. */
    @GET("api/matches/updates")
    suspend fun obtenerActualizaciones(@Query("since") desde: String): ActualizacionPartidosDto
}
