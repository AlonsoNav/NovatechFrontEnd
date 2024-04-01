package com.app.novatech.model

import java.io.Serializable

data class Project(
    val nombre: String,
    val presupuesto: Double,
    val estado: Estado,
    val descripcion: String,
    val fechaInicio: String,
    val responsable: Responsable,
    val tareas: List<Tasks>,
    val cambios: List<Logs>,
    val recursos: List<Resource>,
    val foro: String,
    val reuniones: List<String>
) : Serializable
