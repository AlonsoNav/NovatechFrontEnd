import com.app.novatech.util.Database

class CollaboratorsAddController {
    companion object {
        private val db = Database()
        fun collaboratorsAddAttempt(id: String, name: String, email: String, department: String,
                                    phone: String, password: String, project: String,
                                    callback: (okhttp3.Response) -> Unit){
            val endpoint ="colaboradores/"
            val json =  """
        {
            "cedula": "$id",
            "nombre": "$name",
            "correo": "$email",
            "departamento": "$department",
            "telefono": "$phone",
            "contrasena": "$password",
            "nombreProyecto": "$project"
        }
    """.trimIndent()

            val collaboratorsAddSuccessful = db.postRequestToApi(json,endpoint) { response ->
                callback(response)
            }
        }
    }
}
