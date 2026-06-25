package com.example.proyecto_final.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.proyecto_final.data.local.entity.GrupoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GrupoDao {

    @Query("SELECT * FROM grupos ORDER BY nombre")
    fun observar(): Flow<List<GrupoEntity>>

    @Query("SELECT * FROM grupos WHERE id = :id")
    fun observarPorId(id: Int): Flow<GrupoEntity?>

    @Upsert
    suspend fun guardarTodos(grupos: List<GrupoEntity>)

    @Query("DELETE FROM grupos")
    suspend fun borrarTodos()

    /** Reemplaza la lista completa de grupos (el usuario pudo salir de alguno). */
    @Transaction
    suspend fun reemplazar(grupos: List<GrupoEntity>) {
        borrarTodos()
        guardarTodos(grupos)
    }
}
