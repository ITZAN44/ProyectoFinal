package com.example.proyecto_final.ui.stadiums

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
import com.example.proyecto_final.domain.model.Estadio
import com.example.proyecto_final.domain.model.Partido
import com.example.proyecto_final.ui.common.estadoPartidoLegible
import com.example.proyecto_final.ui.common.formatearFecha

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalleEstadio(
    alVolver: () -> Unit,
    alAbrirPartido: (Int) -> Unit,
    viewModel: DetalleEstadioViewModel = hiltViewModel()
) {
    val estado by viewModel.estado.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(estado.estadio?.nombre ?: "Estadio") },
                navigationIcon = {
                    IconButton(onClick = alVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        // Primera carga sin datos en cache: spinner centrado.
        if (estado.cargando && estado.estadio == null && estado.partidos.isEmpty()) {
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
            estado.estadio?.let { estadio ->
                item { DatosEstadio(estadio) }
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

            item {
                Text(text = "Partidos", style = MaterialTheme.typography.titleMedium)
            }
            if (estado.partidos.isEmpty()) {
                item { Text("No hay partidos en esta sede.") }
            } else {
                items(estado.partidos, key = { it.id }) { partido ->
                    FilaPartido(partido, onClick = { alAbrirPartido(partido.id) })
                }
            }
        }
    }
}

@Composable
private fun DatosEstadio(estadio: Estadio) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(text = "${estadio.ciudad}, ${estadio.pais}", style = MaterialTheme.typography.bodyLarge)
        Text(
            text = "Capacidad: ${"%,d".format(estadio.capacidad)}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun FilaPartido(partido: Partido, onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${partido.equipoLocal}  vs  ${partido.equipoVisitante}",
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            if (partido.estado == "finished" && partido.golesLocal != null && partido.golesVisitante != null) {
                Text(text = "${partido.golesLocal} - ${partido.golesVisitante}", fontWeight = FontWeight.Bold)
            }
        }
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
