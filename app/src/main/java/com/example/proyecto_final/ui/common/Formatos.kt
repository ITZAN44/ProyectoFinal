package com.example.proyecto_final.ui.common

/** Formatea una fecha ISO ("2026-06-25T18:00:00.000000Z") a "25/06/2026 18:00". */
fun formatearFecha(iso: String): String = runCatching {
    val fecha = iso.substring(0, 10)
    val hora = iso.substring(11, 16)
    val (anio, mes, dia) = fecha.split("-")
    "$dia/$mes/$anio $hora"
}.getOrDefault(iso)

/** Traduce el estado del partido a una etiqueta legible. */
fun estadoPartidoLegible(estado: String): String = when (estado) {
    "scheduled" -> "Programado"
    "live" -> "En vivo"
    "finished" -> "Finalizado"
    else -> estado
}

/** Traduce la fase del Mundial a una etiqueta legible. */
fun faseLegible(fase: String): String = when (fase) {
    "group" -> "Fase de grupos"
    "round_of_32" -> "Dieciseisavos"
    "round_of_16" -> "Octavos"
    "quarter" -> "Cuartos"
    "semi" -> "Semifinal"
    "third_place" -> "Tercer puesto"
    "final" -> "Final"
    else -> fase
}
