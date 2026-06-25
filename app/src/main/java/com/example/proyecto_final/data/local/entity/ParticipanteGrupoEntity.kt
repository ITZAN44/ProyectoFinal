package com.example.proyecto_final.data.local.entity

import androidx.room.Entity

/** Integrante de un grupo con su puntaje y posición en la clasificación. */
@Entity(tableName = "participantes_grupo", primaryKeys = ["grupoId", "idUsuario"])
data class ParticipanteGrupoEntity(
    val grupoId: Int,
    val idUsuario: Int,
    val nombre: String,
    val puntaje: Int,
    val posicion: Int
)
