package com.example.proyecto_final.ui.stadiums

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_final.data.repository.RepositorioEstadios
import com.example.proyecto_final.domain.model.Estadio
import com.example.proyecto_final.domain.model.Partido
import com.example.proyecto_final.ui.common.mensajeDeError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Estado del Detalle del Estadio. Datos desde Room; cargando/error son del sync. */
data class EstadoDetalleEstadio(
    val estadio: Estadio? = null,
    val partidos: List<Partido> = emptyList(),
    val cargando: Boolean = true,
    val error: String? = null
)

private data class EstadoSincronizacionDetalle(
    val cargando: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class DetalleEstadioViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repositorioEstadios: RepositorioEstadios
) : ViewModel() {

    private val estadioId: Int = checkNotNull(savedStateHandle["estadioId"])

    private val sincronizacion = MutableStateFlow(EstadoSincronizacionDetalle())

    val estado: StateFlow<EstadoDetalleEstadio> = combine(
        repositorioEstadios.observarDetalleEstadio(estadioId),
        repositorioEstadios.observarPartidosDeEstadio(estadioId),
        sincronizacion
    ) { estadio, partidos, sync ->
        EstadoDetalleEstadio(
            estadio = estadio,
            partidos = partidos,
            cargando = sync.cargando,
            error = sync.error
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), EstadoDetalleEstadio())

    init {
        sincronizar()
    }

    fun sincronizar() {
        viewModelScope.launch {
            sincronizacion.value = EstadoSincronizacionDetalle(cargando = true, error = null)
            val resultado = runCatching { repositorioEstadios.sincronizarDetalle(estadioId) }
            sincronizacion.value = EstadoSincronizacionDetalle(
                cargando = false,
                error = resultado.exceptionOrNull()?.let { mensajeDeError(it) }
            )
        }
    }
}
