package ru.ok.itmo.tamtam.server

import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.GET
import ru.ok.itmo.tamtam.server.dto.LoginRequest

object Requests {
    const val LOGIN = "/login"
    const val CHANNELS = "/channels"
    const val LOGOUT = "/logout"
}

val requestsWithTokenSet: Set<String> = setOf(
    Requests.LOGOUT
)

interface ServerApi {
    @POST(Requests.LOGIN)
    @Headers("Content-Type: application/json")
    fun login(@Body requestBody: LoginRequest): Flow<ResponseBody>

    @GET(Requests.CHANNELS)
    fun getChannels(): Flow<List<String>>

    @POST(Requests.LOGOUT)
    fun logout(): Flow<ResponseBody>

    companion object {
        fun provideServerApi(retrofit: Retrofit): ServerApi {
            return retrofit.create(ServerApi::class.java)
        }
    }
}