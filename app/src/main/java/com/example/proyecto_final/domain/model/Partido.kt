package com.example.proyecto_final.domain.model

/** Partido del Mundial. Los goles son nulos mientras el partido no tenga resultado. */
data class Partido(
    val id: Int,
    val equipoLocal: String,
    val equipoVisitante: String,
    val fecha: String,
    val fase: String?,
    val estado: String,
    val golesLocal: Int?,
    val golesVisitante: Int?,
    val estadioId: Int?,
    val estadioNombre: String?,
    val estadioCiudad: String?
)
