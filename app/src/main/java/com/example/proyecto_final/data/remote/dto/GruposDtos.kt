package com.example.proyecto_final.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CrearGrupoRequest(val name: String)

@Serializable
data class GrupoCreadoDto(
    val id: Int,
    val name: String,
    @SerialName("invite_code") val inviteCode: String
)

@Serializable
data class UnirseGrupoRequest(
    @SerialName("invite_code") val codigoInvitacion: String
)

@Serializable
data class UnirseGrupoDto(val message: String)

/**
 * Detalle de un grupo. Solo modelamos lo que la pantalla usa: los próximos partidos.
 * El nombre y el código ya están en la lista de grupos; la clasificación llega por el leaderboard.
 * OJO: el server usa `next_games`, NO `next_matches`.
 */
@Serializable
data class DetalleGrupoDto(
    @SerialName("next_games") val proximosPartidos: List<PartidoDto> = emptyList()
)

/** Una entrada del leaderboard de un grupo: posición, usuario, nombre y puntaje. */
@Serializable
data class LeaderboardEntradaDto(
    val position: Int,
    val id: Int,
    val name: String,
    val score: Int
)
