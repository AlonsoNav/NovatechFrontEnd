package com.app.novatech.util

import android.util.Log
import com.app.novatech.model.Colaborador
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray

class CollaboratorController {
    companion object {
        private val client = OkHttpClient()

        suspend fun obtenerColaboradores(): List<Colaborador> = withContext(Dispatchers.IO) {
            val endpoint = "https://backend-ap.fly.dev/api/colaboradores"
            val request = Request.Builder().url(endpoint).build()
            val response = client.newCall(request).execute()

            val responseBody = response.body?.string()

            Log.d("obtenerColaboradores", "Respuesta: $responseBody")

            if (response.isSuccessful && responseBody != null) {
                try {
                    val jsonArray = JSONArray(responseBody)
                    val colaboradoresList = mutableListOf<Colaborador>()

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val nombre = jsonObject.getString("nombre")
                        val email = jsonObject.optString("email", "No especificado")
                        colaboradoresList.add(Colaborador(nombre, email))
                    }
                    colaboradoresList
                } catch (e: Exception) {
                    Log.e("obtenerColaboradores", "Error al parsear JSON", e)
                    emptyList()
                }
            } else {
                // Log para respuesta fallida
                Log.d("obtenerColaboradores", "Respuesta fallida: ${response.code}")
                emptyList()
            }
        }
    }
}
