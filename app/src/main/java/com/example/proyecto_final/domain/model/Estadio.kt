package com.example.proyecto_final.domain.model

/** Sede oficial del Mundial. */
data class Estadio(
    val id: Int,
    val nombre: String,
    val ciudad: String,
    val pais: String,
    val latitud: Double,
    val longitud: Double,
    val capacidad: Int
)
