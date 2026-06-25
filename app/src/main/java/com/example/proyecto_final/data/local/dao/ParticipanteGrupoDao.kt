package com.example.proyecto_final.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.proyecto_final.data.local.entity.ParticipanteGrupoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ParticipanteGrupoDao {

    @Query("SELECT * FROM participantes_grupo WHERE grupoId = :grupoId ORDER BY posicion ASC")
    fun observarPorGrupo(grupoId: Int): Flow<List<ParticipanteGrupoEntity>>

    @Upsert
    suspend fun guardarTodos(participantes: List<ParticipanteGrupoEntity>)

    @Query("DELETE FROM participantes_grupo WHERE grupoId = :grupoId")
    suspend fun borrarDeGrupo(grupoId: Int)

    /** Reemplaza la clasificación de un grupo (alguien pudo entrar o salir). */
    @Transaction
    suspend fun reemplazarDeGrupo(grupoId: Int, participantes: List<ParticipanteGrupoEntity>) {
        borrarDeGrupo(grupoId)
        guardarTodos(participantes)
    }
}
