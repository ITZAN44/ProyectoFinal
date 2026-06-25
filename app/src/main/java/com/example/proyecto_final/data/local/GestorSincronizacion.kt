package com.example.proyecto_final.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/** DataStore con la metadata de sincronización (independiente de la sesión del usuario). */
private val Context.dataStoreSync by preferencesDataStore(name = "sincronizacion")

@Singleton
class GestorSincronizacion @Inject constructor(
    @ApplicationContext private val contexto: Context
) {

    private val clavePartidosSincronizadoEn = stringPreferencesKey("partidos_synced_at")

    /** Última marca de sincronización del calendario, o null si nunca se sincronizó. */
    suspend fun partidosSincronizadoEn(): String? =
        contexto.dataStoreSync.data.map { it[clavePartidosSincronizadoEn] }.first()

    suspend fun guardarPartidosSincronizadoEn(marca: String) {
        contexto.dataStoreSync.edit { it[clavePartidosSincronizadoEn] = marca }
    }
}
