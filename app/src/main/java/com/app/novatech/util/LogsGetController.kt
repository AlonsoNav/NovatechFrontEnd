package com.app.novatech.util

class LogsGetController {
    companion object {
        private val db = Database()
        fun getLogsAttempt(name: String, callback: (okhttp3.Response) -> Unit){
            val endpoint ="proyectos/$name/cambios"
            val getLogsSuccessful = db.getRequestToApi(endpoint) { response ->
                callback(response)
            }
        }
    }
}