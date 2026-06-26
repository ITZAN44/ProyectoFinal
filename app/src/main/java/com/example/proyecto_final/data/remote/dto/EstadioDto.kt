package com.example.proyecto_final.data.remote.dto

import kotlinx.serialization.Serializable

/** Estadio completo tal como lo devuelve `GET /stadiums`. */
@Serializable
data class EstadioDto(
    val id: Int,
    val name: String,
    val city: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val capacity: Int
)
