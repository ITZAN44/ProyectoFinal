package com.example.proyecto_final.data.repository

import com.example.proyecto_final.data.aDominio
import com.example.proyecto_final.data.aEntity
import com.example.proyecto_final.data.local.dao.PerfilDao
import com.example.proyecto_final.data.remote.ApiService
import com.example.proyecto_final.domain.model.Perfil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/** Perfil del usuario, offline-first (la UI lee de Room). */
class RepositorioPerfil @Inject constructor(
    private val apiService: ApiService,
    private val perfilDao: PerfilDao
) {

    val perfil: Flow<Perfil?> = perfilDao.observar().map { it?.aDominio() }

    suspend fun sincronizar() {
        val dto = apiService.obtenerPerfil()
        perfilDao.guardar(dto.aEntity())
    }
}
