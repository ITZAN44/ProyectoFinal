package com.example.proyecto_final.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Body de `POST /predictions` (upsert por `match_id`). */
@Serializable
data class CrearPronosticoRequest(
    @SerialName("match_id") val matchId: Int,
    @SerialName("home_score") val golesLocal: Int,
    @SerialName("away_score") val golesVisitante: Int
)

/** Respuesta 201 de `POST /predictions`. Solo se usa el mensaje; el resto se re-sincroniza. */
@Serializable
data class PronosticoCreadoDto(val message: String)

/** Una entrada de `GET /predictions/me`. El `match` embebido se ignora (ya está en `partidos`). */
@Serializable
data class PronosticoMeDto(
    @SerialName("match_id") val matchId: Int,
    @SerialName("home_score") val golesLocal: Int,
    @SerialName("away_score") val golesVisitante: Int,
    @SerialName("points_earned") val puntosObtenidos: Int,
    val status: String
)
