package com.example.proyecto_final.ui.matches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_final.data.repository.RepositorioPartidos
import com.example.proyecto_final.data.repository.RepositorioPronosticos
import com.example.proyecto_final.domain.model.Partido
import com.example.proyecto_final.domain.model.Pronostico
import com.example.proyecto_final.ui.common.mensajeDeError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Filtros del calendario. Un valor nulo significa "todos". */
data class FiltrosPartidos(
    val fase: String? = null,
    val estado: String? = null,
    val fecha: String? = null
)

data class EstadoPartidos(
    val partidos: List<Partido> = emptyList(),
    val fechasDisponibles: List<String> = emptyList(),
    val pronosticos: Map<Int, Pronostico> = emptyMap(),
    val filtros: FiltrosPartidos = FiltrosPartidos(),
    val cargando: Boolean = true,
    val error: String? = null
)

private data class EstadoSincronizacion(
    val cargando: Boolean = true,
    val error: String? = null
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PartidosViewModel @Inject constructor(
    private val repositorioPartidos: RepositorioPartidos,
    private val repositorioPronosticos: RepositorioPronosticos
) : ViewModel() {

    private val filtros = MutableStateFlow(FiltrosPartidos())
    private val sincronizacion = MutableStateFlow(EstadoSincronizacion())

    val estado: StateFlow<EstadoPartidos> = combine(
        filtros.flatMapLatest { f ->
            repositorioPartidos.observarPartidos(f.fase, f.estado, f.fecha)
        },
        repositorioPartidos.observarFechas(),
        filtros,
        sincronizacion,
        repositorioPronosticos.pronosticos
    ) { partidos, fechas, f, sync, pronosticos ->
        EstadoPartidos(
            partidos = partidos,
            fechasDisponibles = fechas,
            pronosticos = pronosticos.associateBy { it.matchId },
            filtros = f,
            cargando = sync.cargando,
            error = sync.error
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), EstadoPartidos())

    init {
        sincronizar()
    }

    fun sincronizar() {
        viewModelScope.launch {
            sincronizacion.value = EstadoSincronizacion(cargando = true, error = null)
            val resultado = runCatching { repositorioPartidos.sincronizarCalendario() }
            // Best-effort: refresca los pronósticos para pintarlos en la lista.
            runCatching { repositorioPronosticos.sincronizar() }
            sincronizacion.value = EstadoSincronizacion(
                cargando = false,
                error = resultado.exceptionOrNull()?.let { mensajeDeError(it) }
            )
        }
    }

    fun cambiarFase(fase: String?) = filtros.update { it.copy(fase = fase) }
    fun cambiarEstado(estado: String?) = filtros.update { it.copy(estado = estado) }
    fun cambiarFecha(fecha: String?) = filtros.update { it.copy(fecha = fecha) }
}
