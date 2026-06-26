package com.example.proyecto_final.data.repository

import com.example.proyecto_final.data.aDominio
import com.example.proyecto_final.data.aEntity
import com.example.proyecto_final.data.local.dao.EstadioDao
import com.example.proyecto_final.data.remote.ApiService
import com.example.proyecto_final.domain.model.Estadio
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/** Sedes del Mundial, offline-first: se leen desde Room y se sincronizan desde la API. */
class RepositorioEstadios @Inject constructor(
    private val apiService: ApiService,
    private val estadioDao: EstadioDao
) {

    val estadios: Flow<List<Estadio>> = estadioDao.observarTodos().map { lista ->
        lista.map { it.aDominio() }
    }

    suspend fun sincronizarEstadios() {
        val dtos = apiService.obtenerEstadios()
        estadioDao.guardarTodos(dtos.map { it.aEntity() })
    }
}
