package com.example.proyecto_final.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.proyecto_final.data.local.entity.EstadioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EstadioDao {

    @Query("SELECT * FROM estadios ORDER BY nombre ASC")
    fun observarTodos(): Flow<List<EstadioEntity>>

    @Query("SELECT * FROM estadios WHERE id = :id")
    fun observarPorId(id: Int): Flow<EstadioEntity?>

    @Upsert
    suspend fun guardarTodos(estadios: List<EstadioEntity>)
}
