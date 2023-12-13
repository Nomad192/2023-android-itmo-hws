package ru.ok.itmo.tamtam.server

import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.GET

object Requests {
    const val LOGIN = "/login"
    const val FIRST_CH = "/1ch"
    const val CHANNELS = "/channels"
    const val LOGOUT = "/logout"
}

val requestsWithTokenSet: Set<String> = setOf(
    Requests.LOGOUT
)

interface ServerApi {
    @POST(Requests.LOGIN)
    @Headers("Content-Type: application/json")
    suspend fun login(@Body requestBody: LoginRequest): ResponseBody

    @GET(Requests.FIRST_CH)
    suspend fun getMessagesFrom1ch(): ResponseBody

    @GET(Requests.CHANNELS)
    suspend fun getChannels(): ResponseBody

    @POST(Requests.LOGOUT)
    suspend fun logout(): ResponseBody
}

data class LoginRequest(
    @SerializedName("name")
    val login: String,

    @SerializedName("pwd")
    val password: String
)