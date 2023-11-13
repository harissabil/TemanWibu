package com.harissabil.anidex.ui.components.others

import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Straight
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.exyte.animatednavbar.items.dropletbutton.DropletButton
import com.harissabil.anidex.R
import com.harissabil.anidex.ui.navigation.NavigationItem
import com.harissabil.anidex.ui.navigation.Screen

@Composable
fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    selectedIndex: Int
) {
//    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

    AnimatedNavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp, start = 16.dp, end = 16.dp, top = 8.dp)
            .height(68.dp),
        selectedIndex = selectedIndex,
        barColor = MaterialTheme.colorScheme.primary,
        ballColor = MaterialTheme.colorScheme.primary,
        cornerRadius = shapeCornerRadius(cornerRadius = 34.dp),
        ballAnimation = Straight(tween(600)),
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

//        LaunchedEffect(key1 = currentRoute) {
//            when(currentRoute) {
//                Screen.Home.route -> selectedIndex = 0
//                Screen.Search.route -> selectedIndex = 1
//                Screen.Library.route -> selectedIndex = 2
//                Screen.Profile.route -> selectedIndex = 3
//            }
//        }

        val navigationItems = listOf(
            NavigationItem(
                title = "Home",
                icon = Icons.Filled.Home,
                screen = Screen.Home,
                index = 0,
                iconDrawable = R.drawable.ic_home
            ),
            NavigationItem(
                title = "Search",
                icon = Icons.Filled.Search,
                screen = Screen.Search,
                index = 1,
                iconDrawable = R.drawable.ic_search
            ),
            NavigationItem(
                title = "Library",
                icon = Icons.Filled.VideoLibrary,
                screen = Screen.Library,
                index = 2,
                iconDrawable = R.drawable.ic_library
            ),
            NavigationItem(
                title = "Forum",
                icon = Icons.Filled.Forum,
                screen = Screen.Forum,
                index = 3,
                iconDrawable = R.drawable.ic_forum
            ),
            NavigationItem(
                title = "Profile",
                icon = Icons.Filled.Person,
                screen = Screen.Profile,
                index = 4,
                iconDrawable = R.drawable.ic_person
            )
        )

        navigationItems.forEach { item ->
            DropletButton(
                isSelected = currentRoute == item.screen.route,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                icon = item.iconDrawable,
                modifier = Modifier.fillMaxSize(),
                size = 24.dp,
                iconColor = if (currentRoute == item.screen.route) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimary.copy(
                    alpha = 0.5f
                ),
                dropletColor = if (currentRoute == item.screen.route) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimary.copy(
                    alpha = 0.5f
                ),
                animationSpec = tween(500)
            )
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .noRippleClickable {
////                        selectedIndex = item.index
//                        navController.navigate(item.screen.route) {
//                            popUpTo(navController.graph.findStartDestination().id) {
//                                saveState = true
//                            }
//                            restoreState = true
//                            launchSingleTop = true
//                        }
//                    },
//                contentAlignment = Alignment.Center
//            ) {
//                Icon(
//                    imageVector = item.icon,
//                    contentDescription = item.title,
//                    tint = if (currentRoute == item.screen.route) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimary.copy(
//                        alpha = 0.5f
//                    )
//                )
//            }
        }
    }
}


fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        onClick()
    }
}