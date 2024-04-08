package com.app.novatech.util

class ProjectsDeleteController {
    companion object {
        private val db = Database()
        fun deleteProjectsAttempt(name: String, callback: (okhttp3.Response) -> Unit){
            val endpoint ="proyectos/$name"
            val deleteProjectsSuccessful = db.deleteRequestToApi(endpoint) { response ->
                callback(response)
            }
        }
    }
}