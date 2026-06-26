package com.example.proyecto_final.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "estadios")
data class EstadioEntity(
    @PrimaryKey val id: Int,
    val nombre: String,
    val ciudad: String,
    val pais: String,
    val latitud: Double,
    val longitud: Double,
    val capacidad: Int
)
