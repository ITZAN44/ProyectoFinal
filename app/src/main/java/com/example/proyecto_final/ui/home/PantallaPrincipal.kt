package com.example.proyecto_final.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.proyecto_final.domain.model.Grupo
import com.example.proyecto_final.domain.model.Partido
import com.example.proyecto_final.domain.model.Perfil
import com.example.proyecto_final.ui.common.estadoPartidoLegible
import com.example.proyecto_final.ui.common.formatearFecha

@Composable
fun PantallaPrincipal(viewModel: PrincipalViewModel = hiltViewModel()) {
    val estado by viewModel.estado.collectAsState()

    // Primera carga sin datos en cache: spinner centrado.
    if (estado.cargando && estado.perfil == null && estado.grupos.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { EncabezadoPerfil(estado.perfil) }

        estado.error?.let { mensaje ->
            item {
                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        item { TituloSeccion("Mis grupos") }
        if (estado.grupos.isEmpty()) {
            item { TextoVacio("Todavía no estás en ningún grupo.") }
        } else {
            items(estado.grupos, key = { it.id }) { grupo -> FilaGrupo(grupo) }
        }

        item { TituloSeccion("Próximos partidos") }
        if (estado.proximosPartidos.isEmpty()) {
            item { TextoVacio("No hay próximos partidos.") }
        } else {
            items(estado.proximosPartidos, key = { it.id }) { partido -> FilaPartido(partido) }
        }
    }
}

@Composable
private fun EncabezadoPerfil(perfil: Perfil?) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Hola, ${perfil?.nombre ?: ""}",
                style = MaterialTheme.typography.headlineSmall
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Estadistica("Puntaje", perfil?.puntajeTotal ?: 0)
                Estadistica("Grupos", perfil?.cantidadGrupos ?: 0)
                Estadistica("Pronósticos", perfil?.cantidadPronosticos ?: 0)
            }
        }
    }
}

@Composable
private fun Estadistica(etiqueta: String, valor: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = valor.toString(), style = MaterialTheme.typography.titleLarge)
        Text(text = etiqueta, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun FilaGrupo(grupo: Grupo) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = grupo.nombre, fontWeight = FontWeight.Medium)
            Text(text = "Puntaje: ${grupo.puntajeUsuario}")
        }
        Text(
            text = "${grupo.cantidadParticipantes} participantes",
            style = MaterialTheme.typography.bodySmall
        )
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
private fun FilaPartido(partido: Partido) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(
            text = "${partido.equipoLocal}  vs  ${partido.equipoVisitante}",
            fontWeight = FontWeight.Medium
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatearFecha(partido.fecha),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = estadoPartidoLegible(partido.estado),
                style = MaterialTheme.typography.bodySmall
            )
        }
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
    }
}

@Composable
private fun TituloSeccion(texto: String) {
    Text(
        text = texto,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 8.dp)
    )
}

@Composable
private fun TextoVacio(texto: String) {
    Text(text = texto, style = MaterialTheme.typography.bodyMedium)
}
