package com.app.novatech.util

class CollaboratorController {
    companion object {
        private val db = Database()
        fun getCollaboratorByCedula(cedula: String, callback: (okhttp3.Response) -> Unit) {
            val endpoint = "colaboradores/$cedula"
            db.getRequestToApi(endpoint) { response ->
                callback(response)
            }
        }
    }
}
