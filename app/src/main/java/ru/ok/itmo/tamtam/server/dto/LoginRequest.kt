package ru.ok.itmo.tamtam.server.dto

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("name")
    val login: String,

    @SerializedName("pwd")
    val password: String
)