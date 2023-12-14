package ru.ok.itmo.tamtam.server

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.ok.itmo.tamtam.BuildConfig
import java.util.concurrent.TimeUnit

object RetrofitProvider {

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .connectTimeout(5, TimeUnit.SECONDS)
        .build()

    private val gson: Gson = GsonBuilder()
        .setPrettyPrinting()
        .create()
    private val gsonConverterFactory: GsonConverterFactory = GsonConverterFactory.create(gson)

    val retrofit: Retrofit by lazy<Retrofit> {

        Retrofit.Builder()
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(FlowCallAdapterFactory.create())
            .baseUrl(BuildConfig.API_URL)
            .client(okHttpClient)
            .build()
    }
}