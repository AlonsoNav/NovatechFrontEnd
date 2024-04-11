package com.app.novatech.util

import com.google.gson.Gson

class TasksEditController {
    companion object {
        private val db = Database()
        fun tasksEditAttempt(projectName: String, name: String, newName: String, description: String,
                             responsible: String, storyPoints: Int?, status: String,
                             callback: (okhttp3.Response) -> Unit){
            val endpoint ="proyectos/$projectName/tareas/$name"
            val json = mutableMapOf<String, Any?>(
                "nombreNuevo" to newName,
                "descripcion" to description,
                "correoResponsable" to responsible,
                "estadoTarea" to status,
                "storyPoints" to storyPoints
            )
            val jsonString = Gson().toJson(json)
            val tasksEditSuccessful = db.putRequestToApi(jsonString,endpoint) { response ->
                callback(response)
            }
        }
    }
}