package com.app.novatech.util

import com.google.gson.Gson

class ProjectsEditController {
    companion object {
        private val db = Database()
        fun projectsEditAttempt(name: String, budget: Double?, description: String?,
                               status: String?, responsible: String?,
                               callback: (okhttp3.Response) -> Unit){
            val endpoint ="proyectos/$name"
            val json = mutableMapOf<String, Any?>(
                "presupuesto" to budget,
                "descripcion" to description,
                "estado" to status,
                "correoResponsable" to responsible
            )
            val jsonString = Gson().toJson(json)
            val projectsEditSuccessful = db.putRequestToApi(jsonString, endpoint) { response ->
                callback(response)
            }
        }
    }
}