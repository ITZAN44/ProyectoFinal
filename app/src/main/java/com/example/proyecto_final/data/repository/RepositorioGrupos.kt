package com.example.proyecto_final.data.repository

import com.example.proyecto_final.data.aDominio
import com.example.proyecto_final.data.aEntity
import com.example.proyecto_final.data.local.dao.GrupoDao
import com.example.proyecto_final.data.remote.ApiService
import com.example.proyecto_final.domain.model.Grupo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/** Grupos del usuario, offline-first. */
class RepositorioGrupos @Inject constructor(
    private val apiService: ApiService,
    private val grupoDao: GrupoDao
) {

    val grupos: Flow<List<Grupo>> = grupoDao.observar().map { lista ->
        lista.map { it.aDominio() }
    }

    suspend fun sincronizar() {
        val dtos = apiService.obtenerGrupos()
        grupoDao.reemplazar(dtos.map { it.aEntity() })
    }
}
