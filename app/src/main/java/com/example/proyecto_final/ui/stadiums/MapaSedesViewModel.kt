package com.example.proyecto_final.ui.stadiums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_final.data.repository.RepositorioEstadios
import com.example.proyecto_final.domain.model.Estadio
import com.example.proyecto_final.ui.common.mensajeDeError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EstadoMapaSedes(
    val estadios: List<Estadio> = emptyList(),
    val cargando: Boolean = true,
    val error: String? = null
)

private data class EstadoSincronizacion(
    val cargando: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class MapaSedesViewModel @Inject constructor(
    private val repositorioEstadios: RepositorioEstadios
) : ViewModel() {

    private val sincronizacion = MutableStateFlow(EstadoSincronizacion())

    val estado: StateFlow<EstadoMapaSedes> = combine(
        repositorioEstadios.estadios,
        sincronizacion
    ) { estadios, sync ->
        EstadoMapaSedes(estadios = estadios, cargando = sync.cargando, error = sync.error)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), EstadoMapaSedes())

    init {
        sincronizar()
    }

    fun sincronizar() {
        viewModelScope.launch {
            sincronizacion.value = EstadoSincronizacion(cargando = true, error = null)
            val resultado = runCatching { repositorioEstadios.sincronizarEstadios() }
            sincronizacion.value = EstadoSincronizacion(
                cargando = false,
                error = resultado.exceptionOrNull()?.let { mensajeDeError(it) }
            )
        }
    }
}
