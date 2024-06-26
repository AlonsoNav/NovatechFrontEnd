package com.app.novatech.util

class LoginController {
    companion object {
        private val db = Database()
        fun loginAttempt(email: String, password: String, callback: (okhttp3.Response) -> Unit){
            val endpoint ="colaboradores/login"
            val json =  """
        {
            "correo": "$email",
            "contrasena": "$password"
        }
    """.trimIndent()
            val loginSuccessful = db.postRequestToApi(json,endpoint) { response ->
                callback(response)
            }
        }
    }
}