package com.app.novatech.util

class CollaboratorsGetController {
    companion object {
        private val db = Database()
        fun getCollaboratorsAttempt(id: String, callback: (okhttp3.Response) -> Unit){
            val endpoint ="colaboradores/$id"
            val getCollaboratorsSuccessful = db.getRequestToApi(endpoint) { response ->
                callback(response)
            }
        }
    }
}