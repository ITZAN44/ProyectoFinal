package com.example.proyecto_final.ui.groups

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_final.data.repository.RepositorioGrupos
import com.example.proyecto_final.data.repository.RepositorioPartidos
import com.example.proyecto_final.domain.model.Grupo
import com.example.proyecto_final.domain.model.ParticipanteClasificado
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

/** Estado del Detalle del Grupo. Los datos vienen de Room; cargando/error son del sync. */
data class EstadoDetalleGrupo(
    val grupo: Grupo? = null,
    val clasificacion: List<ParticipanteClasificado> = emptyList(),
    val proximosPartidos: List<Partido> = emptyList(),
    val cargando: Boolean = true,
    val error: String? = null
)

private data class EstadoSincronizacion(
    val cargando: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class DetalleGrupoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repositorioGrupos: RepositorioGrupos,
    repositorioPartidos: RepositorioPartidos
) : ViewModel() {

    private val grupoId: Int = checkNotNull(savedStateHandle["grupoId"])

    private val sincronizacion = MutableStateFlow(EstadoSincronizacion())

    val estado: StateFlow<EstadoDetalleGrupo> = combine(
        repositorioGrupos.observarGrupo(grupoId),
        repositorioGrupos.observarClasificacion(grupoId),
        repositorioPartidos.proximosPartidos,
        sincronizacion
    ) { grupo, clasificacion, partidos, sync ->
        EstadoDetalleGrupo(
            grupo = grupo,
            clasificacion = clasificacion,
            proximosPartidos = partidos,
            cargando = sync.cargando,
            error = sync.error
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), EstadoDetalleGrupo())

    init {
        sincronizar()
    }

    fun sincronizar() {
        viewModelScope.launch {
            sincronizacion.value = EstadoSincronizacion(cargando = true, error = null)
            val resultado = runCatching { repositorioGrupos.sincronizarDetalle(grupoId) }
            sincronizacion.value = EstadoSincronizacion(
                cargando = false,
                error = resultado.exceptionOrNull()?.let { mensajeDeError(it) }
            )
        }
    }
}
