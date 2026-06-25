package com.example.proyecto_final.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Pronóstico del usuario para un partido. Se cruza con `partidos` por `matchId`. */
@Entity(tableName = "pronosticos")
data class PronosticoEntity(
    @PrimaryKey val matchId: Int,
    val golesLocal: Int,
    val golesVisitante: Int,
    val puntosObtenidos: Int,
    val estado: String
)
