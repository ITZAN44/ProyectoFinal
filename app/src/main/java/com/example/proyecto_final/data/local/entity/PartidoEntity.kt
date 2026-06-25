package com.example.proyecto_final.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "partidos")
data class PartidoEntity(
    @PrimaryKey val id: Int,
    val equipoLocal: String,
    val equipoVisitante: String,
    val fecha: String,
    val fase: String?,
    val estado: String,
    val golesLocal: Int?,
    val golesVisitante: Int?
)
