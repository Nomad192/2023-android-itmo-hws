package ru.ok.itmo.tamtam.server

sealed interface RequestUiState {
    open class Success: RequestUiState
    open class Wait: RequestUiState
    open class Error(val e: Throwable) : RequestUiState
}