package com.example.proyecto_final.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_final.data.repository.RepositorioAuth
import com.example.proyecto_final.ui.common.mensajeDeError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Estado de la pantalla de inicio de sesión. */
data class EstadoInicioSesion(
    val email: String = "",
    val contrasena: String = "",
    val cargando: Boolean = false,
    val error: String? = null,
    val sesionIniciada: Boolean = false
)

@HiltViewModel
class InicioSesionViewModel @Inject constructor(
    private val repositorioAuth: RepositorioAuth
) : ViewModel() {

    private val _estado = MutableStateFlow(EstadoInicioSesion())
    val estado: StateFlow<EstadoInicioSesion> = _estado.asStateFlow()

    private val regexEmail = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")

    fun alCambiarEmail(valor: String) {
        _estado.update { it.copy(email = valor, error = null) }
    }

    fun alCambiarContrasena(valor: String) {
        _estado.update { it.copy(contrasena = valor, error = null) }
    }

    fun iniciarSesion() {
        val email = _estado.value.email.trim()
        val contrasena = _estado.value.contrasena

        val errorValidacion = validar(email, contrasena)
        if (errorValidacion != null) {
            _estado.update { it.copy(error = errorValidacion) }
            return
        }

        viewModelScope.launch {
            _estado.update { it.copy(cargando = true, error = null) }
            val resultado = repositorioAuth.iniciarSesion(email, contrasena)
            _estado.update { estadoActual ->
                resultado.fold(
                    onSuccess = { estadoActual.copy(cargando = false, sesionIniciada = true) },
                    onFailure = { error -> estadoActual.copy(cargando = false, error = mensajeDeError(error)) }
                )
            }
        }
    }

    private fun validar(email: String, contrasena: String): String? = when {
        email.isBlank() || contrasena.isBlank() -> "Completá correo y contraseña."
        !regexEmail.matches(email) -> "Ingresá un correo válido."
        else -> null
    }
}
