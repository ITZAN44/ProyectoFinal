package com.example.proyecto_final.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GrupoDto(
    val id: Int,
    val name: String,
    @SerialName("participants_count") val participantsCount: Int,
    @SerialName("user_score") val userScore: Int,
    @SerialName("invite_code") val inviteCode: String
)
