package com.app.novatech.util

class CollaboratorsDeleteController {
    companion object {
        private val db = Database()
        fun deleteCollaboratorsAttempt(id: String, callback: (okhttp3.Response) -> Unit){
            val endpoint ="colaboradores/$id"
            val deleteCollaboratorsSuccessful = db.deleteRequestToApi(endpoint) { response ->
                callback(response)
            }
        }
    }
}