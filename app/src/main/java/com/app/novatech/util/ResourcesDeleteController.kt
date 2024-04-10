package com.app.novatech.util

class ResourcesDeleteController {
    companion object {
        private val db = Database()
        fun deleteResourcesAttempt(nameProject: String, nameResource: String, callback: (okhttp3.Response) -> Unit){
            val endpoint ="proyectos/$nameProject/recursos/$nameResource"
            val deleteResourcesSuccessful = db.deleteRequestToApi(endpoint) { response ->
                callback(response)
            }
        }
    }
}