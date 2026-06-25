package com.example.proyecto_final.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.proyecto_final.ui.groups.PantallaMisGrupos
import com.example.proyecto_final.ui.home.PantallaPrincipal
import com.example.proyecto_final.ui.profile.PantallaPerfil

/** Secciones del menú principal. Por ahora solo "Inicio" está implementada. */
private enum class SeccionMenu(val titulo: String, val icono: ImageVector) {
    Inicio("Inicio", Icons.Default.Home),
    Grupos("Grupos", Icons.Default.Person),
    Partidos("Partidos", Icons.Default.DateRange),
    Mapa("Mapa", Icons.Default.Place),
    Perfil("Perfil", Icons.Default.AccountCircle)
}

/** Shell con barra de navegación inferior que aloja las pantallas principales. */
@Composable
fun MenuPrincipal(alCerrarSesion: () -> Unit) {
    var seccion by rememberSaveable { mutableStateOf(SeccionMenu.Inicio) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                SeccionMenu.entries.forEach { item ->
                    NavigationBarItem(
                        selected = seccion == item,
                        onClick = { seccion = item },
                        icon = { Icon(item.icono, contentDescription = item.titulo) },
                        label = { Text(item.titulo) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (seccion) {
                SeccionMenu.Inicio -> PantallaPrincipal()
                SeccionMenu.Grupos -> PantallaMisGrupos()
                SeccionMenu.Partidos -> Placeholder("Partidos")
                SeccionMenu.Mapa -> Placeholder("Mapa de sedes")
                SeccionMenu.Perfil -> PantallaPerfil(onSesionCerrada = alCerrarSesion)
            }
        }
    }
}

@Composable
private fun Placeholder(titulo: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("$titulo (próximamente)")
    }
}
