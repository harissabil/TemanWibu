package com.harissabil.anidex.data.remote.projekbasdat.dto.auth

data class LoginResponse(
    val data: List<Data>?,
    val message: String,
    val status: String
)

data class Data(
    val username: String,
    val password: String,
    val name: String,
    val email: String
)