package ru.ok.itmo.tamtam.server

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.ok.itmo.tamtam.server.dto.LoginRequest

class ServerWorker(private val serverApi: ServerApi) {

    fun login(login: String, password: String): Flow<String> {
        return serverApi.login(LoginRequest(login, password)).map { it.string() }
    }

    fun getChannels(): Flow<List<String>> {
        return serverApi.getChannels()
    }

    fun logout(): Flow<Unit> {
        return serverApi.logout().map {  }
    }

    companion object {
        fun getInstance(): ServerWorker
        {
            val retrofit = RetrofitProvider.retrofit
            val weatherApi = ServerApi.provideServerApi(retrofit)
            return ServerWorker(weatherApi)
        }
    }
}