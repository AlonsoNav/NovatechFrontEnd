package com.app.novatech.util

class CollaboratorsEditController {
    companion object {
        private val db = Database()
        fun editCollaboratorsAttempt(id: String, email: String, department: String, phone: String,
                                     newPassword: String, password: String,
                                     callback: (okhttp3.Response) -> Unit){
            val endpoint ="colaboradores/$id"
            val json =  """
        {
            "correo": "$email",
            "departamento": "$department",
            "telefono": "$phone",
            "nuevaContrasena": "$newPassword",
            "contrasena": "$password"
        }
    """.trimIndent()
            val editCollaboratorsSuccessful = db.putRequestToApi(json, endpoint) { response ->
                callback(response)
            }
        }
    }
}