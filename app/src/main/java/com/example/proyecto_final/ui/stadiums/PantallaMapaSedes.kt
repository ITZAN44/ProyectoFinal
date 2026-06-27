package com.example.proyecto_final.ui.stadiums

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/** Vista continental por defecto (Norteamérica: sede del Mundial 2026). */
private val CENTRO_DEFECTO = LatLng(39.5, -98.35)

@Composable
fun PantallaMapaSedes(
    alAbrirSede: (Int) -> Unit = {},
    viewModel: MapaSedesViewModel = hiltViewModel()
) {
    val estado by viewModel.estado.collectAsState()
    val contexto = LocalContext.current

    var permisoConcedido by remember { mutableStateOf(tienePermisoUbicacion(contexto)) }
    val lanzadorPermiso = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido -> permisoConcedido = concedido }

    LaunchedEffect(Unit) {
        if (!permisoConcedido) lanzadorPermiso.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(CENTRO_DEFECTO, 3f)
    }

    // Centra el mapa en la ubicación del dispositivo una vez concedido el permiso.
    LaunchedEffect(permisoConcedido) {
        if (permisoConcedido) {
            obtenerUltimaUbicacion(contexto)?.let { ubicacion ->
                camara.animate(CameraUpdateFactory.newLatLngZoom(ubicacion, 5f))
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = camara,
            properties = MapProperties(isMyLocationEnabled = permisoConcedido),
            uiSettings = MapUiSettings(myLocationButtonEnabled = permisoConcedido)
        ) {
            estado.estadios.forEach { estadio ->
                Marker(
                    state = rememberMarkerState(
                        key = estadio.id.toString(),
                        position = LatLng(estadio.latitud, estadio.longitud)
                    ),
                    title = estadio.nombre,
                    snippet = "${estadio.ciudad}, ${estadio.pais} · ${"%,d".format(estadio.capacidad)}",
                    onInfoWindowClick = { alAbrirSede(estadio.id) }
                )
            }
        }

        // Primera carga sin sedes en cache: spinner centrado sobre el mapa.
        if (estado.cargando && estado.estadios.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        // Sin sedes y con error de sincronización: aviso (offline sin cache previa).
        estado.error?.let { mensaje ->
            if (estado.estadios.isEmpty() && !estado.cargando) {
                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

private fun tienePermisoUbicacion(contexto: Context): Boolean {
    val fina = ContextCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_FINE_LOCATION)
    val gruesa = ContextCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_COARSE_LOCATION)
    return fina == PackageManager.PERMISSION_GRANTED || gruesa == PackageManager.PERMISSION_GRANTED
}

/** Última ubicación conocida del dispositivo, o null si no hay o falla. */
@SuppressLint("MissingPermission")
private suspend fun obtenerUltimaUbicacion(contexto: Context): LatLng? =
    suspendCancellableCoroutine { continuacion ->
        LocationServices.getFusedLocationProviderClient(contexto).lastLocation
            .addOnSuccessListener { ubicacion ->
                continuacion.resume(ubicacion?.let { LatLng(it.latitude, it.longitude) })
            }
            .addOnFailureListener { continuacion.resume(null) }
    }
