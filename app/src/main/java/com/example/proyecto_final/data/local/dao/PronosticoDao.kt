package com.example.proyecto_final.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.proyecto_final.data.local.entity.PronosticoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PronosticoDao {

    @Query("SELECT * FROM pronosticos WHERE matchId = :matchId")
    fun observarPorPartido(matchId: Int): Flow<PronosticoEntity?>

    @Upsert
    suspend fun guardar(pronostico: PronosticoEntity)

    @Upsert
    suspend fun guardarTodos(pronosticos: List<PronosticoEntity>)

    @Query("DELETE FROM pronosticos")
    suspend fun borrarTodos()

    /** Reemplaza todos los pronósticos (predictions/me es la fuente completa del usuario). */
    @Transaction
    suspend fun reemplazar(pronosticos: List<PronosticoEntity>) {
        borrarTodos()
        guardarTodos(pronosticos)
    }
}
