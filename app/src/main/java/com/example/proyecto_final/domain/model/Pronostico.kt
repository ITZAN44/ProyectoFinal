package com.example.proyecto_final.domain.model

/** Pronóstico del usuario para un partido. `puntosObtenidos` se resuelve al finalizar. */
data class Pronostico(
    val matchId: Int,
    val golesLocal: Int,
    val golesVisitante: Int,
    val puntosObtenidos: Int,
    val estado: String
)
