package ru.ok.itmo.tamtam.server

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ServerWorker {
    companion object {
        fun login(login: String, password: String): Flow<String> = flow {
            val loginRequest = LoginRequest(login = login, password = password)
            val responseBody = RetrofitClient.apiService.login(loginRequest)
            val token = responseBody.string()
            emit(token)
        }.flowOn(Dispatchers.IO)

        fun getMessagesFrom1ch(): Flow<String> = flow {
            val responseBody = RetrofitClient.apiService.getMessagesFrom1ch()
            val result = responseBody.string()
            emit(result)
        }.flowOn(Dispatchers.IO)

        fun getChannels(): Flow<String> = flow {
            val responseBody = RetrofitClient.apiService.getChannels()
            val result = responseBody.string()
            emit(result)
        }.flowOn(Dispatchers.IO)

        fun logout(): Flow<String> = flow {
            val responseBody = RetrofitClient.apiService.logout()
            val result = responseBody.string()
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
}