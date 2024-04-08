package com.app.novatech.model

import java.io.Serializable

data class Project(
    val nombre: String,
    val presupuesto: Double,
    val estado: String,
    val descripcion: String,
    val fechaInicio: String,
    val fechaFin: String,
    val responsable: String,
) : Serializable
