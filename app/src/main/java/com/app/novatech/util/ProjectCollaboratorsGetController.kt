package com.app.novatech.util

class ProjectCollaboratorsGetController {
    companion object {
        private val db = Database()
        fun getProjectCollaboratorsAttempt(name: String, callback: (okhttp3.Response) -> Unit){
            val endpoint ="proyectos/$name/colab"
            val getProjectCollaboratorsSuccessful = db.getRequestToApi(endpoint) { response ->
                callback(response)
            }
        }
    }
}