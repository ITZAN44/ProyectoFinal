package com.example.proyecto_final.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Perfil del usuario. Tabla de una sola fila (id fijo en 0). */
@Entity(tableName = "perfil")
data class PerfilEntity(
    @PrimaryKey val id: Int = 0,
    val nombre: String,
    val email: String,
    val puntajeTotal: Int,
    val cantidadGrupos: Int,
    val cantidadPronosticos: Int
)
