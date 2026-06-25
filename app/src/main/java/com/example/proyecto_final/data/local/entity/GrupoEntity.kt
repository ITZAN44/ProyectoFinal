package com.example.proyecto_final.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grupos")
data class GrupoEntity(
    @PrimaryKey val id: Int,
    val nombre: String,
    val cantidadParticipantes: Int,
    val puntajeUsuario: Int,
    val codigoInvitacion: String
)
