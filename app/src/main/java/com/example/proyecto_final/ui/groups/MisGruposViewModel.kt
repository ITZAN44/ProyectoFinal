package com.example.proyecto_final.ui.groups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_final.data.repository.RepositorioGrupos
import com.example.proyecto_final.domain.model.Grupo
import com.example.proyecto_final.ui.common.mensajeDeError
import com.example.proyecto_final.ui.common.mensajeErrorUnirseGrupo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Estado de una operación de crear o unirse a un grupo. */
data class EstadoOperacion(
    val procesando: Boolean = false,
    val error: String? = null,
    val exito: Boolean = false
)

@HiltViewModel
class MisGruposViewModel @Inject constructor(
    private val repositorioGrupos: RepositorioGrupos
) : ViewModel() {

    val grupos: StateFlow<List<Grupo>> = repositorioGrupos.grupos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _operacion = MutableStateFlow(EstadoOperacion())
    val operacion: StateFlow<EstadoOperacion> = _operacion.asStateFlow()

    init {
        viewModelScope.launch { runCatching { repositorioGrupos.sincronizar() } }
    }

    fun crearGrupo(nombre: String) {
        if (nombre.isBlank()) {
            _operacion.value = EstadoOperacion(error = "Ingresá un nombre.")
            return
        }
        viewModelScope.launch {
            _operacion.value = EstadoOperacion(procesando = true)
            _operacion.value = repositorioGrupos.crearGrupo(nombre.trim()).fold(
                onSuccess = { EstadoOperacion(exito = true) },
                onFailure = { EstadoOperacion(error = mensajeDeError(it)) }
            )
        }
    }

    fun unirseAGrupo(codigo: String) {
        if (codigo.isBlank()) {
            _operacion.value = EstadoOperacion(error = "Ingresá un código.")
            return
        }
        viewModelScope.launch {
            _operacion.value = EstadoOperacion(procesando = true)
            _operacion.value = repositorioGrupos.unirseAGrupo(codigo.trim()).fold(
                onSuccess = { EstadoOperacion(exito = true) },
                onFailure = { EstadoOperacion(error = mensajeErrorUnirseGrupo(it)) }
            )
        }
    }

    fun limpiarOperacion() {
        _operacion.value = EstadoOperacion()
    }
}
