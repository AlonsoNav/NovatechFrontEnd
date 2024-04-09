package com.app.novatech.model

import java.io.Serializable

data class Collaborator(
    val name: String,
    val email: String,
    val id: String
) : Serializable

