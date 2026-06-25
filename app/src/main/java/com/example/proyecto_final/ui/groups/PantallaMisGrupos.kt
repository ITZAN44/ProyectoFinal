package com.example.proyecto_final.ui.groups

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.proyecto_final.domain.model.Grupo

private enum class Dialogo { Crear, Unirse }

@Composable
fun PantallaMisGrupos(
    alAbrirGrupo: (Int) -> Unit,
    viewModel: MisGruposViewModel = hiltViewModel()
) {
    val grupos by viewModel.grupos.collectAsState()
    val operacion by viewModel.operacion.collectAsState()
    var dialogo by remember { mutableStateOf<Dialogo?>(null) }

    LaunchedEffect(operacion.exito) {
        if (operacion.exito) {
            dialogo = null
            viewModel.limpiarOperacion()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.limpiarOperacion(); dialogo = Dialogo.Crear },
                modifier = Modifier.weight(1f)
            ) { Text("Crear grupo") }
            OutlinedButton(
                onClick = { viewModel.limpiarOperacion(); dialogo = Dialogo.Unirse },
                modifier = Modifier.weight(1f)
            ) { Text("Unirse") }
        }

        Spacer(Modifier.height(16.dp))

        if (grupos.isEmpty()) {
            Text("Todavía no estás en ningún grupo.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(grupos, key = { it.id }) { grupo ->
                    FilaGrupo(grupo, onClick = { alAbrirGrupo(grupo.id) })
                }
            }
        }
    }

    when (dialogo) {
        Dialogo.Crear -> DialogoTexto(
            titulo = "Crear grupo",
            etiqueta = "Nombre del grupo",
            textoBoton = "Crear",
            procesando = operacion.procesando,
            error = operacion.error,
            onConfirmar = viewModel::crearGrupo,
            onCerrar = { dialogo = null; viewModel.limpiarOperacion() }
        )
        Dialogo.Unirse -> DialogoTexto(
            titulo = "Unirse a un grupo",
            etiqueta = "Código de invitación",
            textoBoton = "Unirse",
            procesando = operacion.procesando,
            error = operacion.error,
            transformar = { it.uppercase() },
            onConfirmar = viewModel::unirseAGrupo,
            onCerrar = { dialogo = null; viewModel.limpiarOperacion() }
        )
        null -> Unit
    }
}

@Composable
private fun FilaGrupo(grupo: Grupo, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(grupo.nombre, style = MaterialTheme.typography.titleMedium)
            Text(
                text = "${grupo.cantidadParticipantes} participantes · Puntaje: ${grupo.puntajeUsuario}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Código: ${grupo.codigoInvitacion}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun DialogoTexto(
    titulo: String,
    etiqueta: String,
    textoBoton: String,
    procesando: Boolean,
    error: String?,
    transformar: (String) -> String = { it },
    onConfirmar: (String) -> Unit,
    onCerrar: () -> Unit
) {
    var texto by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { if (!procesando) onCerrar() },
        title = { Text(titulo) },
        text = {
            Column {
                OutlinedTextField(
                    value = texto,
                    onValueChange = { texto = transformar(it) },
                    label = { Text(etiqueta) },
                    singleLine = true,
                    enabled = !procesando
                )
                if (error != null) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirmar(texto) }, enabled = !procesando) {
                if (procesando) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                } else {
                    Text(textoBoton)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onCerrar, enabled = !procesando) { Text("Cancelar") }
        }
    )
}
