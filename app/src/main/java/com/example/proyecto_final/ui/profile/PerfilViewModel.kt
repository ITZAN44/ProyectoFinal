package com.example.proyecto_final.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_final.data.repository.RepositorioAuth
import com.example.proyecto_final.data.repository.RepositorioPerfil
import com.example.proyecto_final.domain.model.Perfil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PerfilViewModel @Inject constructor(
    private val repositorioPerfil: RepositorioPerfil,
    private val repositorioAuth: RepositorioAuth
) : ViewModel() {

    val perfil: StateFlow<Perfil?> = repositorioPerfil.perfil
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    init {
        // Refresca el perfil al abrir; si no hay red, se muestra lo de Room.
        viewModelScope.launch { runCatching { repositorioPerfil.sincronizar() } }
    }

    /** Limpia la sesión. El redirect al login lo hace el observador del token en NavegacionApp. */
    fun cerrarSesion() {
        viewModelScope.launch { repositorioAuth.cerrarSesion() }
    }
}
