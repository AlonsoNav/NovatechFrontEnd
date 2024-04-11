package com.app.novatech.util

import com.google.gson.Gson

class TasksAddController {
    companion object {
        private val db = Database()
        fun tasksAddAttempt(projectName: String, name: String, description: String, responsible: String,
                            storyPoints: Int?, callback: (okhttp3.Response) -> Unit){
            val endpoint ="proyectos/$projectName/tareas"
            val json = mutableMapOf<String, Any?>(
                "nombre" to name,
                "descripcion" to description,
                "correoResponsable" to responsible,
                "storyPoints" to storyPoints
            )
            val jsonString = Gson().toJson(json)
            val tasksAddSuccessful = db.postRequestToApi(jsonString,endpoint) { response ->
                callback(response)
            }
        }
    }
}