package com.app.novatech.util

class ProjectsAddController {
    companion object {
        private val db = Database()
        fun projectsAddAttempt(name: String, budget: String, department: String,
                                    phone: String, password: String, project: String,
                                    callback: (okhttp3.Response) -> Unit){
            val endpoint ="colaboradores/"
            val json =  """
        {
            "nombre": "$name",
            "correo": "$budget",
            "departamento": "$department",
            "telefono": "$phone",
            "contrasena": "$password",
            "nombreProyecto": "$project"
        }
    """.trimIndent()

            val projectsAddSuccessful = db.postRequestToApi(json,endpoint) { response ->
                callback(response)
            }
        }
    }
}