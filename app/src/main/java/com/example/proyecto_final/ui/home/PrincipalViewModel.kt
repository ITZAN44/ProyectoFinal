package com.example.proyecto_final.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_final.data.repository.RepositorioGrupos
import com.example.proyecto_final.data.repository.RepositorioPartidos
import com.example.proyecto_final.data.repository.RepositorioPerfil
import com.example.proyecto_final.domain.model.Grupo
import com.example.proyecto_final.domain.model.Partido
import com.example.proyecto_final.domain.model.Perfil
import com.example.proyecto_final.ui.common.mensajeDeError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Estado de la Pantalla Principal. Los datos vienen de Room; cargando/error son del sync. */
data class EstadoPrincipal(
    val perfil: Perfil? = null,
    val grupos: List<Grupo> = emptyList(),
    val proximosPartidos: List<Partido> = emptyList(),
    val cargando: Boolean = true,
    val error: String? = null
)

private data class EstadoSincronizacion(
    val cargando: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class PrincipalViewModel @Inject constructor(
    private val repositorioPerfil: RepositorioPerfil,
    private val repositorioGrupos: RepositorioGrupos,
    private val repositorioPartidos: RepositorioPartidos
) : ViewModel() {

    private val sincronizacion = MutableStateFlow(EstadoSincronizacion())

    val estado: StateFlow<EstadoPrincipal> = combine(
        repositorioPerfil.perfil,
        repositorioGrupos.grupos,
        repositorioPartidos.proximosPartidos,
        sincronizacion
    ) { perfil, grupos, partidos, sync ->
        EstadoPrincipal(
            perfil = perfil,
            grupos = grupos,
            proximosPartidos = partidos,
            cargando = sync.cargando,
            error = sync.error
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), EstadoPrincipal())

    init {
        sincronizar()
    }

    fun sincronizar() {
        viewModelScope.launch {
            sincronizacion.value = EstadoSincronizacion(cargando = true, error = null)
            val resultados = coroutineScope {
                listOf(
                    async { runCatching { repositorioPerfil.sincronizar() } },
                    async { runCatching { repositorioGrupos.sincronizar() } },
                    async { runCatching { repositorioPartidos.sincronizarProximos() } }
                ).awaitAll()
            }
            val error = resultados.firstNotNullOfOrNull { it.exceptionOrNull() }
            sincronizacion.value = EstadoSincronizacion(
                cargando = false,
                error = error?.let { mensajeDeError(it) }
            )
        }
    }
}
