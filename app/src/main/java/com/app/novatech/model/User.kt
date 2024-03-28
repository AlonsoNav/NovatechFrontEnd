package com.app.novatech.model

data class User(
    val cedula: String,
    val nombre: String,
    val correo: String,
    val departamento: String,
    val telefono: String,
    val admin: Boolean
)
