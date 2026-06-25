package com.example.proyecto_final.domain.model

/** Grupo de quiniela al que pertenece el usuario, con su puntaje en él. */
data class Grupo(
    val id: Int,
    val nombre: String,
    val cantidadParticipantes: Int,
    val puntajeUsuario: Int,
    val codigoInvitacion: String
)
