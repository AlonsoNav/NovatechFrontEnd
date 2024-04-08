package com.app.novatech.util

class CollaboratorsListController {
    companion object {
        private val db = Database()
        fun getCollaboratorsListAttempt(callback: (okhttp3.Response) -> Unit){
            val endpoint ="colaboradores/"
            val getCollaboratorsListSuccessful = db.getRequestToApi(endpoint) { response ->
                callback(response)
            }
        }
    }
}