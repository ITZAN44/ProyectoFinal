package com.example.proyecto_final.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PartidoDto(
    val id: Int,
    @SerialName("home_team") val homeTeam: String,
    @SerialName("away_team") val awayTeam: String,
    @SerialName("match_date") val matchDate: String,
    val phase: String? = null,
    val status: String,
    @SerialName("home_score") val homeScore: Int? = null,
    @SerialName("away_score") val awayScore: Int? = null,
    val stadium: EstadioResumenDto? = null
)

/** Estadio tal como viene anidado en un partido. */
@Serializable
data class EstadioResumenDto(
    val id: Int,
    val name: String,
    val city: String
)

/** Respuesta de `GET /matches/updates`. OJO: el array se llama `games`, no `matches`. */
@Serializable
data class ActualizacionPartidosDto(
    @SerialName("synced_at") val sincronizadoEn: String,
    val games: List<PartidoDto>
)
