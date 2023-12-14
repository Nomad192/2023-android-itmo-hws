package ru.ok.itmo.tamtam.server

import java.io.IOException

sealed class ServerException : IOException() {
    data object Unauthorized : ServerException()
    data object NoNet : ServerException()
}