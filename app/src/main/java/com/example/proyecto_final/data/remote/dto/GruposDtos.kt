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
