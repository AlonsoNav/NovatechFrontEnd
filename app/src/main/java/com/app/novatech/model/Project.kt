package com.app.novatech.model

data class Proyecto(
    val nombre: String,
    val presupuesto: Double,
    val estado: String,
    val descripcion: String,
    val fechaInicio: String,
    val responsable: String,
    val tareas: List<Tarea>,
    val cambios: List<Cambio>,
    val recursos: List<Recurso>,
    val foro: String,
    val reuniones: List<String>
    )

data class Recurso(
    val nombre: String,
    val cantidad: Int
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