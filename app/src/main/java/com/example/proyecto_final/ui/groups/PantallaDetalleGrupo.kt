package com.example.proyecto_final.ui.groups

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.proyecto_final.domain.model.ParticipanteClasificado
import com.example.proyecto_final.domain.model.Partido
import com.example.proyecto_final.ui.common.estadoPartidoLegible
import com.example.proyecto_final.ui.common.formatearFecha

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalleGrupo(
    alVolver: () -> Unit,
    alAbrirPartido: (Int) -> Unit,
    viewModel: DetalleGrupoViewModel = hiltViewModel()
) {
    val estado by viewModel.estado.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(estado.grupo?.nombre ?: "Grupo") },
                navigationIcon = {
                    IconButton(onClick = alVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        // Primera carga sin datos en cache: spinner centrado.
        if (estado.cargando && estado.clasificacion.isEmpty() && estado.grupo == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            estado.grupo?.let { grupo ->
                item {
                    Text(
                        text = "Código: ${grupo.codigoInvitacion}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            estado.error?.let { mensaje ->
                item {
                    Text(
                        text = mensaje,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            item { TituloSeccion("Clasificación") }
            if (estado.clasificacion.isEmpty()) {
                item { Text("Todavía no hay participantes.") }
            } else {
                items(estado.clasificacion, key = { it.idUsuario }) { participante ->
                    FilaClasificacion(participante)
                }
            }

            item { TituloSeccion("Próximos partidos") }
            if (estado.proximosPartidos.isEmpty()) {
                item { Text("No hay próximos partidos.") }
            } else {
                items(estado.proximosPartidos, key = { it.id }) { partido ->
                    FilaPartido(partido, onClick = { alAbrirPartido(partido.id) })
                }
            }
        }
    }
}

@Composable
private fun FilaClasificacion(participante: ParticipanteClasificado) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "${participante.posicion}. ${participante.nombre}", fontWeight = FontWeight.Medium)
        Text(text = "${participante.puntaje} pts")
    }
}

@Composable
private fun FilaPartido(partido: Partido, onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Text(
            text = "${partido.equipoLocal}  vs  ${partido.equipoVisitante}",
            fontWeight = FontWeight.Medium
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = formatearFecha(partido.fecha), style = MaterialTheme.typography.bodySmall)
            Text(text = estadoPartidoLegible(partido.estado), style = MaterialTheme.typography.bodySmall)
        }
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
private fun TituloSeccion(texto: String) {
    Text(text = texto, style = MaterialTheme.typography.titleMedium)
}
