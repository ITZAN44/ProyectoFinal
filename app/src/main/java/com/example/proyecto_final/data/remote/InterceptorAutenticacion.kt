package com.example.proyecto_final.data.remote

import com.example.proyecto_final.data.local.GestorSesion
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Agrega el header Accept y, si hay sesión activa, el token Bearer a cada solicitud.
 */
class InterceptorAutenticacion @Inject constructor(
    private val gestorSesion: GestorSesion
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { gestorSesion.token.first() }
        val solicitud = chain.request().newBuilder()
            .addHeader("Accept", "application/json")
            .apply {
                if (!token.isNullOrBlank()) {
                    addHeader("Authorization", "Bearer $token")
                }
            }
            .build()
        val respuesta = chain.proceed(solicitud)
        // Token revocado o expirado: el server responde 401. Limpiamos la sesión para forzar
        // el reingreso. Solo ante un 401 real (con token enviado): sin internet la llamada falla
        // con IOException, NO con 401, así el modo offline nunca cierra la sesión.
        if (respuesta.code == 401 && !token.isNullOrBlank()) {
            runBlocking { gestorSesion.limpiarSesion() }
        }
        return respuesta
    }
}
