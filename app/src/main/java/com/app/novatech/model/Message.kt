package com.app.novatech.model

import java.io.Serializable

data class Message(
    val mensaje: String,
    val tiempo: String,
    val colaborador: String
) : Serializable
