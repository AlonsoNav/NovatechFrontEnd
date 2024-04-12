package com.app.novatech.util

class ProjectCollaboratorsDeleteController {
    companion object {
        private val db = Database()
        fun deleteProjectCollaboratorsAttempt(name: String, email: String, callback: (okhttp3.Response) -> Unit){
            val endpoint ="proyectos/$name/colab/$email"
            val deleteProjectCollaboratorsSuccessful = db.deleteRequestToApi(endpoint) { response ->
                callback(response)
            }
        }
    }
}