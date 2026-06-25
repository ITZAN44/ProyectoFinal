package com.example.proyecto_final.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.proyecto_final.data.local.dao.GrupoDao
import com.example.proyecto_final.data.local.dao.ParticipanteGrupoDao
import com.example.proyecto_final.data.local.dao.PartidoDao
import com.example.proyecto_final.data.local.dao.PerfilDao
import com.example.proyecto_final.data.local.dao.PronosticoDao
import com.example.proyecto_final.data.local.entity.GrupoEntity
import com.example.proyecto_final.data.local.entity.ParticipanteGrupoEntity
import com.example.proyecto_final.data.local.entity.PartidoEntity
import com.example.proyecto_final.data.local.entity.PerfilEntity
import com.example.proyecto_final.data.local.entity.PronosticoEntity

@Database(
    entities = [
        PerfilEntity::class,
        GrupoEntity::class,
        PartidoEntity::class,
        ParticipanteGrupoEntity::class,
        PronosticoEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun perfilDao(): PerfilDao
    abstract fun grupoDao(): GrupoDao
    abstract fun partidoDao(): PartidoDao
    abstract fun participanteGrupoDao(): ParticipanteGrupoDao
    abstract fun pronosticoDao(): PronosticoDao
}
