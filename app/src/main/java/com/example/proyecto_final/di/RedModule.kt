package com.example.proyecto_final.di

import com.example.proyecto_final.data.remote.ApiService
import com.example.proyecto_final.data.remote.InterceptorAutenticacion
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

/** Módulo Hilt que provee las dependencias de red (OkHttp, Retrofit, ApiService). */
@Module
@InstallIn(SingletonComponent::class)
object RedModule {

    private const val URL_BASE = "http://quiniela.jmacboy.com/"

    @Provides
    @Singleton
    fun proveerJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun proveerClienteHttp(interceptorAutenticacion: InterceptorAutenticacion): OkHttpClient {
        val registro = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(interceptorAutenticacion)
            .addInterceptor(registro)
            .build()
    }

    @Provides
    @Singleton
    fun proveerRetrofit(clienteHttp: OkHttpClient, json: Json): Retrofit {
        return Retrofit.Builder()
            .baseUrl(URL_BASE)
            .client(clienteHttp)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun proveerApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)
}
