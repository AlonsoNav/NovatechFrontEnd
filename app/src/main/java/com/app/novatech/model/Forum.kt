package com.app.novatech.model

import java.io.Serializable

data class Forum(
    val cedula: String,
    val nombre: String,
    val correo: String,
    val departamento: String,
    val telefono: String,
    val admin: Boolean,
    val proyecto: Project?
) : Serializable
