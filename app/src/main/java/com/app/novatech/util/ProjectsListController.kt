package com.app.novatech.util

class ProjectsListController {
    companion object {
        private val db = Database()
        fun getProjectsListAttempt(callback: (okhttp3.Response) -> Unit){
            val endpoint ="proyectos/"
            val getProjectsListSuccessful = db.getRequestToApi(endpoint) { response ->
                callback(response)
            }
        }
    }
}