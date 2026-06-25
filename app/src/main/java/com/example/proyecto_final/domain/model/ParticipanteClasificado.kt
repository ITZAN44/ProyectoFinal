package com.example.proyecto_final.domain.model

/** Integrante de un grupo dentro de la clasificación: posición, nombre y puntaje. */
data class ParticipanteClasificado(
    val idUsuario: Int,
    val nombre: String,
    val puntaje: Int,
    val posicion: Int
)
