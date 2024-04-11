package com.app.novatech.util

import com.google.gson.Gson

class ProjectsAddController {
    companion object {
        private val db = Database()
        fun projectsAddAttempt(name: String, budget: Double?, description: String,
                                    startDate: String, endDate: String, responsible: String,
                                    callback: (okhttp3.Response) -> Unit){
            val endpoint ="proyectos/"
            val json = mutableMapOf<String, Any?>(
                "nombre" to name,
                "presupuesto" to budget,
                "descripcion" to description,
                "fechaInicio" to startDate,
                "fechaFin" to endDate,
                "correoResponsable" to responsible
            )
            val jsonString = Gson().toJson(json)
            val projectsAddSuccessful = db.postRequestToApi(jsonString, endpoint) { response ->
                callback(response)
            }
        }
    }
}