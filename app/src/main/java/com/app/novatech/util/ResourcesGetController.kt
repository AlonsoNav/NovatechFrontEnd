package com.app.novatech.util

class ResourcesGetController {
    companion object {
        private val db = Database()
        fun getResourcesAttempt(name: String, callback: (okhttp3.Response) -> Unit){
            val endpoint ="proyectos/$name/recursos"
            val getResourcesSuccessful = db.getRequestToApi(endpoint) { response ->
                callback(response)
            }
        }
    }
}