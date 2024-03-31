import android.content.Context
import android.util.Log
import com.app.novatech.util.Database
import okhttp3.Response

class CollaboratorRegistrationController {
    companion object {
        private val db = Database()
        private const val TAG = "CollabRegController"

        fun registrarColaborador(
            nombre: String,
            cedula: String,
            correo: String,
            telefono: String,
            departamento: String,
            proyecto: String?,
            contrasena: String,
            context: Context,
            callback: (Response, String?) -> Unit
        ){
            val proyectoValue = if (proyecto.isNullOrEmpty()) "null" else "\"$proyecto\""
            val endpoint = "colaboradores/"
            val json = """
                {
                    "nombre": "$nombre",
                    "cedula": "$cedula",
                    "correo": "$correo",
                    "telefono": "$telefono",
                    "departamento": "$departamento",
                    "proyecto": $proyectoValue,
                    "contrasena": "$contrasena"
                }
                """.trimIndent()

            db.postRequestToApi(json, endpoint) { response ->
                val responseBody: String? = response.body?.string()

                if (!response.isSuccessful) {
                    Log.e(TAG, "Failed to register collaborator: HTTP ${response.code} - $responseBody")
                }
                callback(response, responseBody)
            }
        }
    }
}
