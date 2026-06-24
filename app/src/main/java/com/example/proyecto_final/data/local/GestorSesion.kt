package com.example.proyecto_final.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/** DataStore que persiste la sesión del usuario (token y datos básicos). */
private val Context.dataStore by preferencesDataStore(name = "sesion")

@Singleton
class GestorSesion @Inject constructor(
    @ApplicationContext private val contexto: Context
) {

    private val claveToken = stringPreferencesKey("token")
    private val claveNombre = stringPreferencesKey("nombre")
    private val claveEmail = stringPreferencesKey("email")

    val token: Flow<String?> = contexto.dataStore.data.map { preferencias ->
        preferencias[claveToken]
    }

    val nombre: Flow<String?> = contexto.dataStore.data.map { preferencias ->
        preferencias[claveNombre]
    }

    val email: Flow<String?> = contexto.dataStore.data.map { preferencias ->
        preferencias[claveEmail]
    }

    suspend fun guardarSesion(token: String, nombre: String, email: String) {
        contexto.dataStore.edit { preferencias ->
            preferencias[claveToken] = token
            preferencias[claveNombre] = nombre
            preferencias[claveEmail] = email
        }
    }

    suspend fun limpiarSesion() {
        contexto.dataStore.edit { preferencias -> preferencias.clear() }
    }
}
