package com.example.proyecto_final.data.repository

import com.example.proyecto_final.data.aDominio
import com.example.proyecto_final.data.aEntity
import com.example.proyecto_final.data.local.GestorSincronizacion
import com.example.proyecto_final.data.local.dao.PartidoDao
import com.example.proyecto_final.data.remote.ApiService
import com.example.proyecto_final.domain.model.Partido
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/** Partidos, offline-first: calendario completo en Room con sincronización incremental. */
class RepositorioPartidos @Inject constructor(
    private val apiService: ApiService,
    private val partidoDao: PartidoDao,
    private val gestorSincronizacion: GestorSincronizacion
) {

    val proximosPartidos: Flow<List<Partido>> = partidoDao.observarProximos().map { lista ->
        lista.map { it.aDominio() }
    }

    /** Calendario filtrado desde Room (los filtros nulos no restringen). */
    fun observarPartidos(fase: String?, estado: String?, fecha: String?): Flow<List<Partido>> =
        partidoDao.observarFiltrados(fase, estado, fecha).map { lista -> lista.map { it.aDominio() } }

    /** Fechas disponibles en el calendario (para el filtro de fecha). */
    fun observarFechas(): Flow<List<String>> = partidoDao.observarFechas()

    /** Un partido por id, desde Room (para el detalle). */
    fun observarPartido(id: Int): Flow<Partido?> =
        partidoDao.observarPorId(id).map { it?.aDominio() }

    /** Refresca un partido puntual desde la API (resultado/estado actualizados). */
    suspend fun sincronizarPartido(id: Int) {
        partidoDao.guardarTodos(listOf(apiService.obtenerDetallePartido(id).aEntity()))
    }

    suspend fun sincronizarProximos() {
        val dtos = apiService.obtenerProximosPartidos()
        partidoDao.guardarTodos(dtos.map { it.aEntity() })
    }

    /**
     * Sincroniza el calendario de forma incremental. La primera vez (sin marca previa) trae
     * todo usando una fecha de origen; luego pide solo lo modificado desde la última marca.
     */
    suspend fun sincronizarCalendario() {
        val desde = gestorSincronizacion.partidosSincronizadoEn() ?: ORIGEN
        val respuesta = apiService.obtenerActualizaciones(desde)
        partidoDao.guardarTodos(respuesta.games.map { it.aEntity() })
        gestorSincronizacion.guardarPartidosSincronizadoEn(respuesta.sincronizadoEn)
    }

    private companion object {
        const val ORIGEN = "1970-01-01T00:00:00Z"
    }
}
