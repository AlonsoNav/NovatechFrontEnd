package com.app.novatech.util

class CollaboratorsGetFreeController {
    companion object {
        private val db = Database()
        fun getFreeCollaboratorsAttempt(callback: (okhttp3.Response) -> Unit){
            val endpoint ="colaboradores/disponibles"
            val getFreeCollaboratorsSuccessful = db.getRequestToApi(endpoint) { response ->
                callback(response)
            }
        }
    }
}