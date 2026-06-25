package com.example.proyecto_final.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PerfilDto(
    val name: String,
    val email: String,
    @SerialName("total_score") val totalScore: Int,
    @SerialName("groups_count") val groupsCount: Int,
    @SerialName("predictions_count") val predictionsCount: Int
)
