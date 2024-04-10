package com.app.novatech.util

import com.google.gson.Gson

class ResourcesAddController {
    companion object {
        private val db = Database()
        fun resourcesAddAttempt(projectName: String, name: String, description: String, type: String,
                                    callback: (okhttp3.Response) -> Unit){
            val endpoint ="proyectos/$projectName/recursos"
        val json = mutableMapOf<String, Any?>(
                "name" to name,
                "description" to description,
                "type" to type
            )
            val jsonString = Gson().toJson(json)
            val resourcesAddSuccessful = db.postRequestToApi(jsonString,endpoint) { response ->
                callback(response)
            }
        }
    }
}