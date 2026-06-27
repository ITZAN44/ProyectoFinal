package com.example.proyecto_final.data.repository

import com.example.proyecto_final.data.aDominio
import com.example.proyecto_final.data.aEntity
import com.example.proyecto_final.data.local.dao.PronosticoDao
import com.example.proyecto_final.data.remote.ApiService
import com.example.proyecto_final.data.remote.dto.CrearPronosticoRequest
import com.example.proyecto_final.domain.model.Pronostico
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/** Pronósticos del usuario, offline-first. */
class RepositorioPronosticos @Inject constructor(
    private val apiService: ApiService,
    private val pronosticoDao: PronosticoDao
) {

    fun observarPronostico(matchId: Int): Flow<Pronostico?> =
        pronosticoDao.observarPorPartido(matchId).map { it?.aDominio() }

    /** Todos los pronósticos del usuario (para mostrarlos en la lista de partidos). */
    val pronosticos: Flow<List<Pronostico>> =
        pronosticoDao.observarTodos().map { lista -> lista.map { it.aDominio() } }

    /** Registra/actualiza el pronóstico (upsert) y refresca desde la API. */
    suspend fun guardarPronostico(matchId: Int, golesLocal: Int, golesVisitante: Int): Result<Unit> =
        runCatching {
            apiService.crearPronostico(
                CrearPronosticoRequest(matchId, golesLocal, golesVisitante)
            )
            sincronizar()
        }

    suspend fun sincronizar() {
        val dtos = apiService.obtenerMisPronosticos()
        pronosticoDao.reemplazar(dtos.map { it.aEntity() })
    }
}
