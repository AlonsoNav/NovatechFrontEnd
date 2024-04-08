package com.app.novatech.util

class ProjectsGetController {
    companion object {
        private val db = Database()
        fun getProjectsAttempt(name: String, callback: (okhttp3.Response) -> Unit){
            val endpoint ="proyectos/$name"
            val getProjectsSuccessful = db.getRequestToApi(endpoint) { response ->
                callback(response)
            }
        }

        fun getColabsAttempt(name: String, callback: (okhttp3.Response) -> Unit) {
            val endpoint = "proyectos/$name/colab"
            db.getRequestToApi(endpoint) { response ->
                callback(response)
            }
        }

    }
}