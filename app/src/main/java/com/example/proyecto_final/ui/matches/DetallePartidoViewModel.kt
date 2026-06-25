package com.example.proyecto_final.ui.matches

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_final.data.repository.RepositorioPartidos
import com.example.proyecto_final.data.repository.RepositorioPronosticos
import com.example.proyecto_final.domain.model.Partido
import com.example.proyecto_final.domain.model.Pronostico
import com.example.proyecto_final.ui.common.mensajeErrorPronostico
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EstadoDetallePartido(
    val partido: Partido? = null,
    val pronostico: Pronostico? = null,
    val guardando: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class DetallePartidoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repositorioPartidos: RepositorioPartidos,
    private val repositorioPronosticos: RepositorioPronosticos
) : ViewModel() {

    private val matchId: Int = checkNotNull(savedStateHandle["matchId"])

    private val operacion = MutableStateFlow(OperacionPronostico())

    val estado: StateFlow<EstadoDetallePartido> = combine(
        repositorioPartidos.observarPartido(matchId),
        repositorioPronosticos.observarPronostico(matchId),
        operacion
    ) { partido, pronostico, op ->
        EstadoDetallePartido(
            partido = partido,
            pronostico = pronostico,
            guardando = op.guardando,
            error = op.error
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), EstadoDetallePartido())

    init {
        viewModelScope.launch {
            runCatching { repositorioPartidos.sincronizarPartido(matchId) }
            runCatching { repositorioPronosticos.sincronizar() }
        }
    }

    fun guardarPronostico(golesLocal: Int, golesVisitante: Int) {
        viewModelScope.launch {
            operacion.value = OperacionPronostico(guardando = true)
            operacion.value = repositorioPronosticos
                .guardarPronostico(matchId, golesLocal, golesVisitante)
                .fold(
                    onSuccess = { OperacionPronostico() },
                    onFailure = { OperacionPronostico(error = mensajeErrorPronostico(it)) }
                )
        }
    }

    private data class OperacionPronostico(
        val guardando: Boolean = false,
        val error: String? = null
    )
}
