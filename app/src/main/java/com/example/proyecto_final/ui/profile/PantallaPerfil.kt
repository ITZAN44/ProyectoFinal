package com.example.proyecto_final.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun PantallaPerfil(
    onSesionCerrada: () -> Unit,
    viewModel: PerfilViewModel = hiltViewModel()
) {
    val perfil by viewModel.perfil.collectAsState()
    val sesionCerrada by viewModel.sesionCerrada.collectAsState()

    LaunchedEffect(sesionCerrada) {
        if (sesionCerrada) onSesionCerrada()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))
        Text(
            text = perfil?.nombre ?: "",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = perfil?.email ?: "",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(24.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                FilaEstadistica("Puntaje acumulado", perfil?.puntajeTotal ?: 0)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                FilaEstadistica("Grupos", perfil?.cantidadGrupos ?: 0)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                FilaEstadistica("Pronósticos realizados", perfil?.cantidadPronosticos ?: 0)
            }
        }

        Spacer(Modifier.weight(1f))
        Button(
            onClick = viewModel::cerrarSesion,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión")
        }
    }
}

@Composable
private fun FilaEstadistica(etiqueta: String, valor: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = etiqueta, style = MaterialTheme.typography.bodyLarge)
        Text(text = valor.toString(), style = MaterialTheme.typography.titleMedium)
    }
}
