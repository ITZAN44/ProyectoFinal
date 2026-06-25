package com.example.proyecto_final.di

import android.content.Context
import androidx.room.Room
import com.example.proyecto_final.data.local.AppDatabase
import com.example.proyecto_final.data.local.dao.GrupoDao
import com.example.proyecto_final.data.local.dao.ParticipanteGrupoDao
import com.example.proyecto_final.data.local.dao.PartidoDao
import com.example.proyecto_final.data.local.dao.PerfilDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Módulo Hilt que provee la base de datos Room y sus DAOs. */
@Module
@InstallIn(SingletonComponent::class)
object BaseDatosModule {

    @Provides
    @Singleton
    fun proveerBaseDatos(@ApplicationContext contexto: Context): AppDatabase =
        Room.databaseBuilder(contexto, AppDatabase::class.java, "quiniela.db")
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()

    @Provides
    fun proveerPerfilDao(baseDatos: AppDatabase): PerfilDao = baseDatos.perfilDao()

    @Provides
    fun proveerGrupoDao(baseDatos: AppDatabase): GrupoDao = baseDatos.grupoDao()

    @Provides
    fun proveerPartidoDao(baseDatos: AppDatabase): PartidoDao = baseDatos.partidoDao()

    @Provides
    fun proveerParticipanteGrupoDao(baseDatos: AppDatabase): ParticipanteGrupoDao =
        baseDatos.participanteGrupoDao()
}
