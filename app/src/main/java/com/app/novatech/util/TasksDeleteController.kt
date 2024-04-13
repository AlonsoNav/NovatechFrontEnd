package com.app.novatech.util

class TasksDeleteController {
    companion object {
        private val db = Database()
        fun deleteTasksAttempt(nameProject: String, nameTask: String, callback: (okhttp3.Response) -> Unit){
            val endpoint ="proyectos/$nameProject/tareas/$nameTask"
            val deleteTasksSuccessful = db.deleteRequestToApi(endpoint) { response ->
                callback(response)
            }
        }
    }
}