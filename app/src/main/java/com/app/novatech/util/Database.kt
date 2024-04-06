package com.app.novatech.util
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class Database {
    fun postRequestToApi(json: String, endpoint: String, callback: (okhttp3.Response) -> Unit){
        val client = OkHttpClient()

        val apiUrl =
            "https://backend-ap.fly.dev/api/$endpoint"

        val requestBody = json.toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(apiUrl)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                callback(response)
            }
        })
    }

    fun getRequestToApi(endpoint: String, callback: (okhttp3.Response) -> Unit){
        val client = OkHttpClient()

        val apiUrl =
            "https://backend-ap.fly.dev/api/$endpoint"

        val request = Request.Builder()
            .url(apiUrl)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                callback(response)
            }
        })
    }

    fun deleteRequestToApi(endpoint: String, callback: (okhttp3.Response) -> Unit){
        val client = OkHttpClient()

        val apiUrl =
            "https://backend-ap.fly.dev/api/$endpoint"

        val request = Request.Builder()
            .url(apiUrl)
            .delete()
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                callback(response)
            }
        })
    }
}