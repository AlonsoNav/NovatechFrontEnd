package com.app.novatech.util

import com.google.gson.Gson

class MeetingsAddController {
    companion object {
        private val db = Database()
        fun addMeetingsAttempt(name: String, date: String, title: String, description: String, medium: String, callback: (okhttp3.Response) -> Unit){
            val endpoint ="reuniones/$name"
            val json = mutableMapOf<String, Any?>(
                "fecha" to date,
                "temaReunion" to title,
                "medioReunion" to medium,
                "descripcion" to description
            )
            val jsonString = Gson().toJson(json)
            val addMeetingsSuccessful = db.postRequestToApi(jsonString ,endpoint) { response ->
                callback(response)
            }
        }
    }
}