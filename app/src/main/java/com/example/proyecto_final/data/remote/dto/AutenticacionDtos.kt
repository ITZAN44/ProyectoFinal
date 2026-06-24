package com.example.proyecto_final.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * DTOs de autenticación. Las claves coinciden con el JSON de la API (manda el contrato).
 */
@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val token: String,
    val name: String,
    val email: String
)
