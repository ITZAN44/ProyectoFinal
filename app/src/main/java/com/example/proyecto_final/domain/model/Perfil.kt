package com.example.proyecto_final.domain.model

/** Perfil del usuario con sus estadísticas generales. */
data class Perfil(
    val nombre: String,
    val email: String,
    val puntajeTotal: Int,
    val cantidadGrupos: Int,
    val cantidadPronosticos: Int
)
