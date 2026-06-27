package com.example.proyecto_final.data.repository

import com.example.proyecto_final.data.aDominio
import com.example.proyecto_final.data.aEntity
import com.example.proyecto_final.data.local.dao.EstadioDao
import com.example.proyecto_final.data.local.dao.PartidoDao
import com.example.proyecto_final.data.remote.ApiService
import com.example.proyecto_final.domain.model.Estadio
import com.example.proyecto_final.domain.model.Partido
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/** Sedes del Mundial, offline-first: se leen desde Room y se sincronizan desde la API. */
class RepositorioEstadios @Inject constructor(
    private val apiService: ApiService,
    private val estadioDao: EstadioDao,
    private val partidoDao: PartidoDao
) {

    val estadios: Flow<List<Estadio>> = estadioDao.observarTodos().map { lista ->
        lista.map { it.aDominio() }
    }

    suspend fun sincronizarEstadios() {
        val dtos = apiService.obtenerEstadios()
        estadioDao.guardarTodos(dtos.map { it.aEntity() })
    }

    /** Detalle de una sede desde Room (para la pantalla de detalle). */
    fun observarDetalleEstadio(id: Int): Flow<Estadio?> =
        estadioDao.observarPorId(id).map { it?.aDominio() }

    /** Partidos de una sede desde Room. */
    fun observarPartidosDeEstadio(id: Int): Flow<List<Partido>> =
        partidoDao.observarPorEstadio(id).map { lista -> lista.map { it.aDominio() } }

    /**
     * Sincroniza el detalle de una sede y sus partidos. El endpoint de partidos NO trae el
     * estadio anidado (lo implica la URL), así que se fuerza `estadioId = id` al guardar para
     * que la query por `estadioId` los encuentre.
     */
    suspend fun sincronizarDetalle(id: Int) {
        estadioDao.guardarTodos(listOf(apiService.obtenerDetalleEstadio(id).aEntity()))
        val partidos = apiService.obtenerPartidosDeEstadio(id)
        partidoDao.guardarTodos(partidos.map { it.aEntity().copy(estadioId = id) })
    }
}
