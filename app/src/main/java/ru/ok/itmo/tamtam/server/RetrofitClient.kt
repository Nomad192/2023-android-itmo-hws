package ru.ok.itmo.tamtam.server

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val SERVER_URL = "https://faerytea.name:8008/"

object RetrofitClient {
    val apiService: ServerApi by lazy {
        Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().addInterceptor(AuthInterceptor()).build())
            .build()
            .create(ServerApi::class.java)
    }
}