package com.example.proyecto_final.data.repository

import com.example.proyecto_final.data.aDominio
import com.example.proyecto_final.data.aEntity
import com.example.proyecto_final.data.local.dao.GrupoDao
import com.example.proyecto_final.data.local.dao.ParticipanteGrupoDao
import com.example.proyecto_final.data.local.dao.PartidoDao
import com.example.proyecto_final.data.remote.ApiService
import com.example.proyecto_final.data.remote.dto.CrearGrupoRequest
import com.example.proyecto_final.data.remote.dto.UnirseGrupoRequest
import com.example.proyecto_final.domain.model.Grupo
import com.example.proyecto_final.domain.model.ParticipanteClasificado
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/** Grupos del usuario, offline-first. */
class RepositorioGrupos @Inject constructor(
    private val apiService: ApiService,
    private val grupoDao: GrupoDao,
    private val participanteGrupoDao: ParticipanteGrupoDao,
    private val partidoDao: PartidoDao
) {

    val grupos: Flow<List<Grupo>> = grupoDao.observar().map { lista ->
        lista.map { it.aDominio() }
    }

    suspend fun sincronizar() {
        val dtos = apiService.obtenerGrupos()
        grupoDao.reemplazar(dtos.map { it.aEntity() })
    }

    /** Cabecera del grupo (nombre, código) desde Room. */
    fun observarGrupo(grupoId: Int): Flow<Grupo?> =
        grupoDao.observarPorId(grupoId).map { it?.aDominio() }

    /** Clasificación del grupo (posición, nombre, puntaje) desde Room. */
    fun observarClasificacion(grupoId: Int): Flow<List<ParticipanteClasificado>> =
        participanteGrupoDao.observarPorGrupo(grupoId).map { lista -> lista.map { it.aDominio() } }

    /**
     * Sincroniza el detalle del grupo: clasificación (leaderboard) y próximos partidos
     * (next_games, que son los próximos del Mundial → tabla de partidos). En paralelo.
     */
    suspend fun sincronizarDetalle(grupoId: Int) = coroutineScope {
        val detalle = async { apiService.obtenerDetalleGrupo(grupoId) }
        val clasificacion = async { apiService.obtenerClasificacion(grupoId) }
        partidoDao.guardarTodos(detalle.await().proximosPartidos.map { it.aEntity() })
        participanteGrupoDao.reemplazarDeGrupo(grupoId, clasificacion.await().map { it.aEntity(grupoId) })
    }

    /** Crea un grupo y refresca la lista local. */
    suspend fun crearGrupo(nombre: String): Result<Unit> = runCatching {
        apiService.crearGrupo(CrearGrupoRequest(name = nombre))
        sincronizar()
    }

    /** Se une a un grupo por código y refresca la lista local. */
    suspend fun unirseAGrupo(codigo: String): Result<Unit> = runCatching {
        apiService.unirseAGrupo(UnirseGrupoRequest(codigoInvitacion = codigo))
        sincronizar()
    }
}
