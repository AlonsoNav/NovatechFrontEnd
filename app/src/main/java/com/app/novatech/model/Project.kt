package com.app.novatech.model

import java.io.Serializable

data class Project(
    val nombre: String,
    val presupuesto: Double,
    val estado: String,
    val descripcion: String,
    val fechaInicio: String,
    val responsable: String,
    val tareas: List<Tasks>,
    val cambios: List<Logs>,
    val recursos: List<Resource>,
    val foro: String,
    val reuniones: List<String>
) : Serializable
