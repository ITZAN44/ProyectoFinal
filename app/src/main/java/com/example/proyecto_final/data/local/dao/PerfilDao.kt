package com.example.proyecto_final.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.proyecto_final.data.local.entity.PerfilEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PerfilDao {

    @Query("SELECT * FROM perfil WHERE id = 0")
    fun observar(): Flow<PerfilEntity?>

    @Upsert
    suspend fun guardar(perfil: PerfilEntity)
}
