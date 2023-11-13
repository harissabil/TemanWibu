package com.harissabil.anidex.data.local.model

data class UserModel(
    val username: String,
    val password: String,
    val name: String,
    val email: String,
    val isLogin: Boolean
)