package com.harissabil.anidex.ui.navigation

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")

    object Home : Screen("home")
    object Search : Screen("search")
    object Library : Screen("library")
    object Forum : Screen("forum")
    object Profile : Screen("profile")
    object Detail : Screen("{from}/{malId}") {
        fun createRoute(from: String, malId: String) = "$from/$malId"
    }
}
