package com.example.proyecto_final.data.repository

import com.example.proyecto_final.data.aDominio
import com.example.proyecto_final.data.aEntity
import com.example.proyecto_final.data.local.dao.PartidoDao
import com.example.proyecto_final.data.remote.ApiService
import com.example.proyecto_final.domain.model.Partido
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/** Partidos, offline-first. En esta fase: próximos partidos para la Home. */
class RepositorioPartidos @Inject constructor(
    private val apiService: ApiService,
    private val partidoDao: PartidoDao
) {

    val proximosPartidos: Flow<List<Partido>> = partidoDao.observarProximos().map { lista ->
        lista.map { it.aDominio() }
    }

    suspend fun sincronizarProximos() {
        val dtos = apiService.obtenerProximosPartidos()
        partidoDao.guardarTodos(dtos.map { it.aEntity() })
    }
}
