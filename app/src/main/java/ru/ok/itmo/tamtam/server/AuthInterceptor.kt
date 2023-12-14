package ru.ok.itmo.tamtam.server

import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.HttpException
import ru.ok.itmo.tamtam.token.TokenModel
import java.net.UnknownHostException

class AuthInterceptor : Interceptor, KoinComponent {
    private val modelInstance: TokenModel by inject()

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val newRequest = if (requestsWithTokenSet.contains(originalRequest.url().encodedPath()))
            originalRequest.newBuilder().header("X-Auth-Token", modelInstance.token).build()
        else
            originalRequest

        try {
            return chain.proceed(newRequest)
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> throw ServerException.Unauthorized
                else -> throw e
            }
        }
        catch (e: UnknownHostException) {
            throw ServerException.NoNet
        }
    }
}