package com.app.novatech.util

class MeetingsDeleteController {
    companion object {
        private val db = Database()
        fun deleteMeetingsAttempt(nameProject: String, nameResource: String, callback: (okhttp3.Response) -> Unit){
            val endpoint ="reuniones/$nameProject/$nameResource"
            val deleteMeetingsSuccessful = db.deleteRequestToApi(endpoint) { response ->
                callback(response)
            }
        }
    }
}