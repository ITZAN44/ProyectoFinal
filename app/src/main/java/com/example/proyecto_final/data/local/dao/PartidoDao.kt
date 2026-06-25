package com.example.proyecto_final.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.proyecto_final.data.local.entity.PartidoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PartidoDao {

    /** Próximos partidos programados (para la Home). */
    @Query("SELECT * FROM partidos WHERE estado = 'scheduled' ORDER BY fecha ASC LIMIT 10")
    fun observarProximos(): Flow<List<PartidoEntity>>

    @Upsert
    suspend fun guardarTodos(partidos: List<PartidoEntity>)
}
