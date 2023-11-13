package com.harissabil.anidex

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.harissabil.anidex.ui.components.others.BottomBar
import com.harissabil.anidex.ui.navigation.Screen
import com.harissabil.anidex.ui.screen.auth.AuthViewModel
import com.harissabil.anidex.ui.screen.auth.login.LoginScreen
import com.harissabil.anidex.ui.screen.auth.register.RegisterScreen
import com.harissabil.anidex.ui.screen.auth.welcome.WelcomeScreen
import com.harissabil.anidex.ui.screen.detail.DetailScreen
import com.harissabil.anidex.ui.screen.forum.ForumScreen
import com.harissabil.anidex.ui.screen.home.HomeScreen
import com.harissabil.anidex.ui.screen.library.LibraryScreen
import com.harissabil.anidex.ui.screen.profile.ProfileScreen
import com.harissabil.anidex.ui.screen.search.SearchScreen
import com.harissabil.anidex.ui.theme.AniDexTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                authViewModel.isLoading.value
            }
        }
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
        )
        setContent {
            val userState by authViewModel.userState.collectAsState()
            Log.d("MainActivity", "onCreate: $userState")

            val windowSize: WindowSizeClass = calculateWindowSizeClass(activity = this)
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

            val startDestination = if (!userState.isLogin) {
                Screen.Welcome.route
            } else {
                Screen.Home.route
            }

            LaunchedEffect(key1 = currentRoute) {
                when (currentRoute) {
                    Screen.Home.route -> selectedIndex = 0
                    Screen.Search.route -> selectedIndex = 1
                    Screen.Library.route -> selectedIndex = 2
                    Screen.Forum.route -> selectedIndex = 3
                    Screen.Profile.route -> selectedIndex = 4
                }
            }

            var showBottomBar by rememberSaveable { mutableStateOf(true) }

            showBottomBar = when (currentRoute) {
                Screen.Detail.route -> false // on this screen bottom bar should be hidden
                Screen.Welcome.route -> false // on this screen bottom bar should be hidden
                Screen.Login.route -> false // on this screen bottom bar should be hidden
                Screen.Register.route -> false // on this screen bottom bar should be hidden
                else -> true // in all other cases show bottom bar
            }

            AniDexTheme {
                Scaffold(
                    bottomBar = {
                        if (showBottomBar) BottomBar(
                            navController = navController,
                            modifier = Modifier.navigationBarsPadding(),
                            selectedIndex = selectedIndex
                        )
                    },
                    contentWindowInsets = WindowInsets(0, 0, 0, 0),
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = startDestination,
                        modifier = Modifier
                            .padding(innerPadding)
                    ) {
                        composable(Screen.Welcome.route) {
                            WelcomeScreen(
                                onLoginClick = {
                                    navController.navigate(Screen.Login.route)
                                },
                                onRegisterClick = {
                                    navController.navigate(Screen.Register.route)
                                }
                            )
                        }
                        composable(Screen.Login.route) {
                            LoginScreen(
                                onNavigateToRegistration = {
                                    navController.navigate(Screen.Register.route)
                                },
                                onNavigateToForgotPassword = {},
                                onNavigateToAuthenticatedRoute = {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(0) // reset stack
                                    }
                                },
                                navigateBack = {
                                    navController.navigateUp()
                                }
                            )
                        }
                        composable(Screen.Register.route) {
                            RegisterScreen(
                                onNavigateToLogin = {
                                    navController.navigate(Screen.Login.route)
                                },
                                onNavigateToAuthenticatedRoute = {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(0) // reset stack
                                    }
                                },
                                navigateBack = {
                                    navController.navigateUp()
                                }
                            )
                        }
                        composable(Screen.Home.route) {
                            HomeScreen(
                                windowSize = windowSize,
                                navigateToDetail = { malId ->
                                    navController.navigate(
                                        Screen.Detail.createRoute(
                                            Screen.Home.route,
                                            malId
                                        )
                                    )
                                },
                            )
                        }
                        composable(Screen.Search.route) {
                            SearchScreen(
                                navigateToDetail = { malId ->
                                    navController.navigate(
                                        Screen.Detail.createRoute(
                                            Screen.Search.route,
                                            malId
                                        )
                                    )
                                }
                            )
                        }
                        composable(Screen.Library.route) {
                            LibraryScreen(
                                navigateToDetail = { malId ->
                                    navController.navigate(
                                        Screen.Detail.createRoute(
                                            Screen.Library.route,
                                            malId
                                        )
                                    )
                                }
                            )
                        }
                        composable(Screen.Forum.route) {
                            ForumScreen(
                                navigateToDetail = { malId ->
                                    navController.navigate(
                                        Screen.Detail.createRoute(
                                            Screen.Library.route,
                                            malId
                                        )
                                    )
                                }
                            )
                        }
                        composable(Screen.Profile.route) {
                            ProfileScreen(
                                viewModel = authViewModel,
                                onLogoutClick = authViewModel::logout
                            )
                        }
                        composable(
                            route = Screen.Detail.route,
                            arguments = listOf(
                                navArgument("malId") { type = NavType.StringType }),
//                            enterTransition = { EnterTransition.None },
//                            exitTransition = { ExitTransition.None }
                        ) {
                            val malId = it.arguments?.getString("malId") ?: ""
                            DetailScreen(
                                malId = malId,
                                navigateBack = {
                                    navController.navigateUp()
                                }
                            )
                        }
                    }
                }
            }
            if (currentRoute != Screen.Welcome.route && currentRoute != Screen.Login.route && currentRoute != Screen.Register.route) {
                launchLoginObserver(navController)
            }
        }
    }

    private fun launchLoginObserver(navController: NavHostController) {
        lifecycleScope.launch(Dispatchers.Main) {
            authViewModel.userState.collect { state ->
                if (!state.isLogin) {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(0) // reset stack
                    }
                }
            }
        }
    }
}