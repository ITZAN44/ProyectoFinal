package com.example.proyecto_final.ui.matches

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.proyecto_final.domain.model.Partido
import com.example.proyecto_final.ui.common.estadoPartidoLegible
import com.example.proyecto_final.ui.common.faseLegible
import com.example.proyecto_final.ui.common.formatearFecha

private val FASES = listOf("group", "round_of_32", "round_of_16", "quarter", "semi", "third_place", "final")
private val ESTADOS = listOf("scheduled", "live", "finished")

@Composable
fun PantallaPartidos(
    alAbrirPartido: (Int) -> Unit,
    viewModel: PartidosViewModel = hiltViewModel()
) {
    val estado by viewModel.estado.collectAsState()
    val filtros = estado.filtros

    Column(modifier = Modifier.fillMaxSize()) {
        FilaFiltro(
            titulo = "Estado",
            opciones = listOf(null to "Todos") + ESTADOS.map { it to estadoPartidoLegible(it) },
            seleccionado = filtros.estado,
            onSeleccionar = viewModel::cambiarEstado
        )
        FilaFiltro(
            titulo = "Fase",
            opciones = listOf(null to "Todas") + FASES.map { it to faseLegible(it) },
            seleccionado = filtros.fase,
            onSeleccionar = viewModel::cambiarFase
        )
        FilaFiltro(
            titulo = "Fecha",
            opciones = listOf(null to "Todas") + estado.fechasDisponibles.map { it to diaCorto(it) },
            seleccionado = filtros.fecha,
            onSeleccionar = viewModel::cambiarFecha
        )

        // Primera carga sin calendario en cache: spinner centrado.
        if (estado.cargando && estado.partidos.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            estado.error?.let { mensaje ->
                item {
                    Text(
                        text = mensaje,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
            if (estado.partidos.isEmpty()) {
                item { Text("No hay partidos para estos filtros.") }
            } else {
                items(estado.partidos, key = { it.id }) { partido ->
                    FilaPartido(partido, onClick = { alAbrirPartido(partido.id) })
                }
            }
        }
    }
}

@Composable
private fun FilaFiltro(
    titulo: String,
    opciones: List<Pair<String?, String>>,
    seleccionado: String?,
    onSeleccionar: (String?) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
        Text(titulo, style = MaterialTheme.typography.labelSmall)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(opciones) { (valor, etiqueta) ->
                FilterChip(
                    selected = seleccionado == valor,
                    onClick = { onSeleccionar(valor) },
                    label = { Text(etiqueta) }
                )
            }
        }
    }
}

@Composable
private fun FilaPartido(partido: Partido, onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 8.dp)) {
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
                Text(
                    text = "${partido.golesLocal} - ${partido.golesVisitante}",
                    fontWeight = FontWeight.Bold
                )
            }
        }
        partido.estadioNombre?.let { nombre ->
            val ciudad = partido.estadioCiudad?.let { ", $it" } ?: ""
            Text("$nombre$ciudad", style = MaterialTheme.typography.bodySmall)
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(formatearFecha(partido.fecha), style = MaterialTheme.typography.bodySmall)
            Text(
                text = partido.fase?.let { faseLegible(it) } ?: estadoPartidoLegible(partido.estado),
                style = MaterialTheme.typography.bodySmall
            )
        }
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
    }
}

/** "2026-06-25" -> "25/06". */
private fun diaCorto(yyyymmdd: String): String = runCatching {
    val (_, mes, dia) = yyyymmdd.split("-")
    "$dia/$mes"
}.getOrDefault(yyyymmdd)
