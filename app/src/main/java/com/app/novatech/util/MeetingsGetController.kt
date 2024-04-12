package com.app.novatech.util

class MeetingsGetController {
    companion object {
        private val db = Database()
        fun getMeetingsAttempt(name: String, callback: (okhttp3.Response) -> Unit){
            val endpoint ="reuniones/$name"
            val getMeetingsSuccessful = db.getRequestToApi(endpoint) { response ->
                callback(response)
            }
        }
    }
}