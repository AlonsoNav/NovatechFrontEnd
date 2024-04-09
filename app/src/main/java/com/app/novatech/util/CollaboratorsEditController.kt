package com.app.novatech.util

import com.google.gson.Gson

class CollaboratorsEditController {
    companion object {
        private val db = Database()
        fun editCollaboratorsAttempt(id: String, email: String?, department: String?, phone: String?,
                                     newPassword: String?, password: String?,
                                     callback: (okhttp3.Response) -> Unit){
            val endpoint ="colaboradores/$id"
            val json = mutableMapOf<String, Any?>(
                "correo" to email,
                "departamento" to department,
                "telefono" to phone,
                "nuevaContrasena" to newPassword,
                "contrasena" to password
            )
            val jsonString = Gson().toJson(json)
            val editCollaboratorsSuccessful = db.putRequestToApi(jsonString, endpoint) { response ->
                callback(response)
            }
        }
    }
}