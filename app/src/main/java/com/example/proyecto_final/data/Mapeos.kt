package com.example.proyecto_final.data

import com.example.proyecto_final.data.local.entity.EstadioEntity
import com.example.proyecto_final.data.local.entity.GrupoEntity
import com.example.proyecto_final.data.local.entity.ParticipanteGrupoEntity
import com.example.proyecto_final.data.local.entity.PartidoEntity
import com.example.proyecto_final.data.local.entity.PerfilEntity
import com.example.proyecto_final.data.local.entity.PronosticoEntity
import com.example.proyecto_final.data.remote.dto.EstadioDto
import com.example.proyecto_final.data.remote.dto.GrupoDto
import com.example.proyecto_final.data.remote.dto.LeaderboardEntradaDto
import com.example.proyecto_final.data.remote.dto.PartidoDto
import com.example.proyecto_final.data.remote.dto.PerfilDto
import com.example.proyecto_final.data.remote.dto.PronosticoMeDto
import com.example.proyecto_final.domain.model.Estadio
import com.example.proyecto_final.domain.model.Grupo
import com.example.proyecto_final.domain.model.Partido
import com.example.proyecto_final.domain.model.ParticipanteClasificado
import com.example.proyecto_final.domain.model.Perfil
import com.example.proyecto_final.domain.model.Pronostico

// --- DTO -> Entity ---

fun PerfilDto.aEntity() = PerfilEntity(
    nombre = name,
    email = email,
    puntajeTotal = totalScore,
    cantidadGrupos = groupsCount,
    cantidadPronosticos = predictionsCount
)

fun GrupoDto.aEntity() = GrupoEntity(
    id = id,
    nombre = name,
    cantidadParticipantes = participantsCount,
    puntajeUsuario = userScore,
    codigoInvitacion = inviteCode
)

fun PartidoDto.aEntity() = PartidoEntity(
    id = id,
    equipoLocal = homeTeam,
    equipoVisitante = awayTeam,
    fecha = matchDate,
    fase = phase,
    estado = status,
    golesLocal = homeScore,
    golesVisitante = awayScore,
    estadioId = stadium?.id,
    estadioNombre = stadium?.name,
    estadioCiudad = stadium?.city
)

fun LeaderboardEntradaDto.aEntity(grupoId: Int) = ParticipanteGrupoEntity(
    grupoId = grupoId,
    idUsuario = id,
    nombre = name,
    puntaje = score,
    posicion = position
)

fun PronosticoMeDto.aEntity() = PronosticoEntity(
    matchId = matchId,
    golesLocal = golesLocal,
    golesVisitante = golesVisitante,
    puntosObtenidos = puntosObtenidos,
    estado = status
)

fun EstadioDto.aEntity() = EstadioEntity(
    id = id,
    nombre = name,
    ciudad = city,
    pais = country,
    latitud = latitude,
    longitud = longitude,
    capacidad = capacity
)

// --- Entity -> Dominio ---

fun PerfilEntity.aDominio() = Perfil(
    nombre = nombre,
    email = email,
    puntajeTotal = puntajeTotal,
    cantidadGrupos = cantidadGrupos,
    cantidadPronosticos = cantidadPronosticos
)

fun GrupoEntity.aDominio() = Grupo(
    id = id,
    nombre = nombre,
    cantidadParticipantes = cantidadParticipantes,
    puntajeUsuario = puntajeUsuario,
    codigoInvitacion = codigoInvitacion
)

fun PartidoEntity.aDominio() = Partido(
    id = id,
    equipoLocal = equipoLocal,
    equipoVisitante = equipoVisitante,
    fecha = fecha,
    fase = fase,
    estado = estado,
    golesLocal = golesLocal,
    golesVisitante = golesVisitante,
    estadioId = estadioId,
    estadioNombre = estadioNombre,
    estadioCiudad = estadioCiudad
)

fun ParticipanteGrupoEntity.aDominio() = ParticipanteClasificado(
    idUsuario = idUsuario,
    nombre = nombre,
    puntaje = puntaje,
    posicion = posicion
)

fun PronosticoEntity.aDominio() = Pronostico(
    matchId = matchId,
    golesLocal = golesLocal,
    golesVisitante = golesVisitante,
    puntosObtenidos = puntosObtenidos,
    estado = estado
)

fun EstadioEntity.aDominio() = Estadio(
    id = id,
    nombre = nombre,
    ciudad = ciudad,
    pais = pais,
    latitud = latitud,
    longitud = longitud,
    capacidad = capacidad
)
