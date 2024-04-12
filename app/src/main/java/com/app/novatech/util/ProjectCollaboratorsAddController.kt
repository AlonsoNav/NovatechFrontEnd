package com.app.novatech.util

import com.google.gson.Gson

class ProjectCollaboratorsAddController {
    companion object {
        private val db = Database()
        fun addProjectCollaboratorsAttempt(name: String, email: String, callback: (okhttp3.Response) -> Unit){
            val endpoint ="proyectos/$name/colab"
            val json = mutableMapOf<String, Any?>(
                "correoColab" to email
            )
            val jsonString = Gson().toJson(json)
            val addProjectCollaboratorsSuccessful = db.postRequestToApi(jsonString ,endpoint) { response ->
                callback(response)
            }
        }
    }
}