package com.app.novatech.util

import com.google.gson.Gson

class CollaboratorsEditAdminController {
    companion object {
        private val db = Database()
        fun editAdminCollaboratorsAttempt(id: String, email: String?, department: String?,
                                          phone: String?, password: String?, project: String?,
                                     callback: (okhttp3.Response) -> Unit){
            val endpoint ="colaboradores/$id/admin"
            val json = mutableMapOf<String, Any?>(
                "correo" to email,
                "departamento" to department,
                "telefono" to phone,
                "nombreProyecto" to project,
                "contrasena" to password
            )
            val jsonString = Gson().toJson(json)
            val editAdminCollaboratorsSuccessful = db.putRequestToApi(jsonString, endpoint) { response ->
                callback(response)
            }
        }
    }
}