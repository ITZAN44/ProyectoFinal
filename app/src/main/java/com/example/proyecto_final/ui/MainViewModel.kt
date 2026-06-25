package com.example.proyecto_final.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_final.data.local.GestorSesion
import com.example.proyecto_final.ui.navigation.Pantalla
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * Decide la pantalla inicial según exista o no una sesión guardada (auto-login).
 * `null` mientras se resuelve la sesión.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    gestorSesion: GestorSesion
) : ViewModel() {

    val rutaInicial: StateFlow<String?> = gestorSesion.token
        .map { token ->
            if (token.isNullOrBlank()) Pantalla.InicioSesion.ruta else Pantalla.Menu.ruta
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )
}
