package com.example.proyecto_final.ui.matches

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.proyecto_final.domain.model.Partido
import com.example.proyecto_final.domain.model.Pronostico
import com.example.proyecto_final.ui.common.estadoPartidoLegible
import com.example.proyecto_final.ui.common.faseLegible
import com.example.proyecto_final.ui.common.formatearFecha

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetallePartido(
    alVolver: () -> Unit,
    viewModel: DetallePartidoViewModel = hiltViewModel()
) {
    val estado by viewModel.estado.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del partido") },
                navigationIcon = {
                    IconButton(onClick = alVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        val partido = estado.partido
        if (partido == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "${partido.equipoLocal}  vs  ${partido.equipoVisitante}",
                style = MaterialTheme.typography.titleLarge
            )
            if (partido.estado == "finished" && partido.golesLocal != null && partido.golesVisitante != null) {
                Text(
                    text = "Resultado: ${partido.golesLocal} - ${partido.golesVisitante}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(formatearFecha(partido.fecha), style = MaterialTheme.typography.bodyMedium)
            partido.fase?.let { Text(faseLegible(it), style = MaterialTheme.typography.bodyMedium) }
            Text(estadoPartidoLegible(partido.estado), style = MaterialTheme.typography.bodyMedium)
            partido.estadioNombre?.let { nombre ->
                val ciudad = partido.estadioCiudad?.let { ", $it" } ?: ""
                Text("$nombre$ciudad", style = MaterialTheme.typography.bodyMedium)
            }

            HorizontalDivider()

            if (partido.estado == "scheduled") {
                FormularioPronostico(
                    pronostico = estado.pronostico,
                    guardando = estado.guardando,
                    error = estado.error,
                    onGuardar = viewModel::guardarPronostico
                )
            } else {
                ResumenPronostico(partido, estado.pronostico)
            }
        }
    }
}

@Composable
private fun FormularioPronostico(
    pronostico: Pronostico?,
    guardando: Boolean,
    error: String?,
    onGuardar: (Int, Int) -> Unit
) {
    var local by remember(pronostico) { mutableStateOf(pronostico?.golesLocal?.toString() ?: "") }
    var visitante by remember(pronostico) { mutableStateOf(pronostico?.golesVisitante?.toString() ?: "") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = if (pronostico == null) "Tu pronóstico" else "Editá tu pronóstico",
            style = MaterialTheme.typography.titleMedium
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CampoGol("Local", local, Modifier.weight(1f)) { local = it }
            CampoGol("Visitante", visitante, Modifier.weight(1f)) { visitante = it }
        }
        error?.let {
            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
        val valido = local.toIntOrNull() != null && visitante.toIntOrNull() != null
        Button(
            onClick = { onGuardar(local.toInt(), visitante.toInt()) },
            enabled = !guardando && valido,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (guardando) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
            } else {
                Text(if (pronostico == null) "Guardar pronóstico" else "Actualizar pronóstico")
            }
        }
    }
}

@Composable
private fun CampoGol(
    etiqueta: String,
    valor: String,
    modifier: Modifier = Modifier,
    onCambio: (String) -> Unit
) {
    OutlinedTextField(
        value = valor,
        onValueChange = { nuevo -> if (nuevo.length <= 2 && nuevo.all { it.isDigit() }) onCambio(nuevo) },
        label = { Text(etiqueta) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier
    )
}

@Composable
private fun ResumenPronostico(partido: Partido, pronostico: Pronostico?) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text("Pronóstico", style = MaterialTheme.typography.titleMedium)
        if (pronostico == null) {
            Text("No pronosticaste este partido.")
        } else {
            Text("Tu pronóstico: ${pronostico.golesLocal} - ${pronostico.golesVisitante}")
            if (partido.estado == "finished") {
                Text(
                    text = "Puntos obtenidos: ${pronostico.puntosObtenidos}",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
