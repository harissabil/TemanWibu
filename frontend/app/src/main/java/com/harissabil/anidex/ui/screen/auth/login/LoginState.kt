package com.harissabil.anidex.ui.screen.auth.login

data class LoginState(
    val message: String = "",
    val isLoading: Boolean = false,
    val isLoginSuccess: Boolean = false
)