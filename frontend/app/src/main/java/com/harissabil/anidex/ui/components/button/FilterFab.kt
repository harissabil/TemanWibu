package com.harissabil.anidex.ui.components.button

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FilterListOff
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.WatchLater
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.harissabil.anidex.ui.screen.library.FilterType
import com.leinardi.android.speeddial.compose.FabWithLabel
import com.leinardi.android.speeddial.compose.SpeedDial
import com.leinardi.android.speeddial.compose.SpeedDialState

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun FilterFab(
    speedDialState: SpeedDialState,
    onFabClick: (Boolean) -> Unit,
    onNoneClick: (FilterType) -> Unit,
    onWishlistClick: (FilterType) -> Unit,
    onWatchlistClick: (FilterType) -> Unit,
    onFinishedClick: (FilterType) -> Unit,
    listState: LazyListState,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    AnimatedVisibility(
        modifier = Modifier.then(modifier),
        visible = !listState.isScrollInProgress,
        enter = slideInVertically {
            with(density) { 40.dp.roundToPx() }
        } + fadeIn(),
        exit = fadeOut(
            animationSpec = keyframes {
                this.durationMillis = 120
            }
        )
    ) {
        SpeedDial(
            state = speedDialState,
            onFabClick = onFabClick,
            fabShape = MaterialTheme.shapes.large,
            fabClosedBackgroundColor = MaterialTheme.colorScheme.primary,
            fabOpenedBackgroundColor = MaterialTheme.colorScheme.primary,
            fabOpenedContentColor = MaterialTheme.colorScheme.onPrimary,
            fabClosedContentColor = MaterialTheme.colorScheme.onPrimary,
            fabOpenedContent = {
                Icon(Icons.Default.FilterList, "Filter", tint = MaterialTheme.colorScheme.onPrimary)
            },
            fabClosedContent = {
                Icon(Icons.Default.FilterList, "Filter", tint = MaterialTheme.colorScheme.onPrimary)
            },
            reverseAnimationOnClose = false,
            fabAnimationRotateAngle = 0f,
        ) {
            item {
                FabWithLabel(
                    onClick = { onNoneClick(FilterType.NONE) },
                    labelContent = { Text(text = "None") },
                    labelBackgroundColor = MaterialTheme.colorScheme.surface,
                    fabShape = MaterialTheme.shapes.large,
                    fabBackgroundColor = MaterialTheme.colorScheme.primary,
                    fabContentColor = MaterialTheme.colorScheme.onPrimary,
                ) {
                    Icon(
                        Icons.Default.FilterListOff,
                        null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            item {
                FabWithLabel(
                    onClick = { onWishlistClick(FilterType.WISHLIST) },
                    labelContent = { Text(text = "Wishlist") },
                    labelBackgroundColor = MaterialTheme.colorScheme.surface,
                    fabShape = MaterialTheme.shapes.large,
                    fabBackgroundColor = MaterialTheme.colorScheme.primary,
                    fabContentColor = MaterialTheme.colorScheme.onPrimary,
                ) {
                    Icon(
                        Icons.Default.FavoriteBorder,
                        null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            item {
                FabWithLabel(
                    onClick = { onWatchlistClick(FilterType.WATCHLIST) },
                    labelContent = { Text(text = "Watchlist") },
                    labelBackgroundColor = MaterialTheme.colorScheme.surface,
                    fabShape = MaterialTheme.shapes.large,
                    fabBackgroundColor = MaterialTheme.colorScheme.primary,
                    fabContentColor = MaterialTheme.colorScheme.onPrimary,
                ) {
                    Icon(
                        Icons.Outlined.WatchLater,
                        null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            item {
                FabWithLabel(
                    onClick = { onFinishedClick(FilterType.FINISHED) },
                    labelContent = { Text(text = "Finished") },
                    labelBackgroundColor = MaterialTheme.colorScheme.surface,
                    fabShape = MaterialTheme.shapes.large,
                    fabBackgroundColor = MaterialTheme.colorScheme.primary,
                    fabContentColor = MaterialTheme.colorScheme.onPrimary,
                ) {
                    Icon(
                        Icons.Outlined.CheckCircle,
                        null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}