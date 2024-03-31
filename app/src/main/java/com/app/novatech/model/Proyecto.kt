package com.app.novatech.model

import java.io.Serializable

data class Proyecto(
    val nombre: String,
    val estado: String,
    val presupuesto: Double,
    val descripcion: String,
    val fechaInicio: String,
    val responsable: String

)

data class Tarea(
    val nombre: String,
    val storyPoints: Int,
    val responsable: String,
    val estado: String
)

data class Cambio(
    val descripcion: String,
    val tiempo: String
)

data class Recurso(
    val nombre: String,
    val cantidad: Int
)

