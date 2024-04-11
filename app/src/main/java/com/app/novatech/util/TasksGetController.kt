package com.app.novatech.util

class TasksGetController {
    companion object {
        private val db = Database()
        fun getTasksAttempt(name: String, callback: (okhttp3.Response) -> Unit){
            val endpoint ="proyectos/$name/tareas"
            val getRTasksSuccessful = db.getRequestToApi(endpoint) { response ->
                callback(response)
            }
        }
    }
}