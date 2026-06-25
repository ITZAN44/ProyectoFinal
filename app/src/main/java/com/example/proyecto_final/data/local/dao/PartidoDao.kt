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

    /** Calendario filtrable. Cada filtro nulo no restringe (se combinan). */
    @Query(
        """
        SELECT * FROM partidos
        WHERE (:fase IS NULL OR fase = :fase)
          AND (:estado IS NULL OR estado = :estado)
          AND (:fecha IS NULL OR substr(fecha, 1, 10) = :fecha)
        ORDER BY fecha ASC
        """
    )
    fun observarFiltrados(fase: String?, estado: String?, fecha: String?): Flow<List<PartidoEntity>>

    /** Fechas distintas del calendario (para el filtro de fecha), formato YYYY-MM-DD. */
    @Query("SELECT DISTINCT substr(fecha, 1, 10) AS dia FROM partidos ORDER BY dia ASC")
    fun observarFechas(): Flow<List<String>>

    @Upsert
    suspend fun guardarTodos(partidos: List<PartidoEntity>)
}
