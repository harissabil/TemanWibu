package com.harissabil.anidex.ui.screen.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.Upcoming
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.harissabil.anidex.data.remote.anime.dto.Data
import com.harissabil.anidex.ui.components.items.AnimeItem
import com.harissabil.anidex.ui.components.others.AssistiveChip
import com.harissabil.anidex.ui.components.others.ErrorMessage
import com.harissabil.anidex.ui.components.others.PaginationLoading
import com.harissabil.anidex.ui.components.others.ScrollToTopButton
import com.harissabil.anidex.ui.theme.spacing
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun HomeScreen(
    windowSize: WindowSizeClass,
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToDetail: (String) -> Unit,
) {
    val state = viewModel.homeScreenState.collectAsState().value
//    val anime = viewModel.anime.collectAsLazyPagingItems()
//    val listState = rememberLazyGridState()
    val snackbarHostState = remember { SnackbarHostState() }

    val listOfLazyGridState: Map<DisplayType, LazyGridState> = mapOf(
        Pair(DisplayType.POPULAR, rememberLazyGridState()),
        Pair(DisplayType.AIRING, rememberLazyGridState()),
        Pair(DisplayType.UPCOMING, rememberLazyGridState())
    )

    val lazyGridState = listOfLazyGridState[state.displayType] ?: rememberLazyGridState()

    val scope = rememberCoroutineScope()
    val showButton: Boolean by remember(lazyGridState) {
        derivedStateOf { lazyGridState.firstVisibleItemIndex > 0 }
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is HomeViewModel.UIEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .statusBarsPadding(),
        topBar = {
            val rowState = rememberScrollState()
            CompositionLocalProvider(
                LocalOverscrollConfiguration provides null
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
//                        .systemBarsPadding()
                        .padding(start = 16.dp, end = 16.dp, bottom = 3.dp)
                        .horizontalScroll(
                            state = rowState
                        ),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                ) {
                    AssistiveChip(isActive = state.displayType == DisplayType.POPULAR,
                        text = { Text(text = "Popular") },
                        icon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                                contentDescription = "Popular"
                            )
                        }) {
                        viewModel.updateDisplayType(DisplayType.POPULAR)
                    }
                    AssistiveChip(isActive = state.displayType == DisplayType.AIRING,
                        text = { Text(text = "Airing") },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.OndemandVideo,
                                contentDescription = "Airing"
                            )
                        }) {
                        viewModel.updateDisplayType(DisplayType.AIRING)
                    }
                    AssistiveChip(isActive = state.displayType == DisplayType.UPCOMING,
                        text = { Text(text = "Upcoming", maxLines = 1) },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Upcoming, contentDescription = "Upcoming"
                            )
                        }) {
                        viewModel.updateDisplayType(DisplayType.UPCOMING)
                    }
                }
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { contentPadding ->
        viewModel.homeScreenState.collectAsState().value.let { state ->
            val anime = state.anime.collectAsLazyPagingItems()
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme.background
                    )
                    .padding(contentPadding)
            ) {
                HomeContent(
                    windowSize = windowSize,
                    listState = lazyGridState,
                    animes = anime,
                    onClick = navigateToDetail
                )
                AnimatedVisibility(
                    visible = showButton,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { fullHeight -> fullHeight }),
                    exit = fadeOut() + slideOutVertically(targetOffsetY = { fullHeight -> fullHeight }),
                    modifier = Modifier
                        .padding(bottom = 30.dp, end = 16.dp)
                        .align(Alignment.BottomEnd)
                ) {
                    ScrollToTopButton(onClick = {
                        scope.launch {
                            lazyGridState.animateScrollToItem(index = 0)
                        }
                    })
                }
            }
            if (anime.loadState.refresh == LoadState.Loading) {
                Log.d("HomeScreen", "HomeScreen: Loading")
                LaunchedEffect(key1 = Unit, block = {
                    listOfLazyGridState[state.displayType]?.scrollToItem(
                        index = 0, scrollOffset = 0
                    )
                })
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContent(
    windowSize: WindowSizeClass,
    listState: LazyGridState,
    animes: LazyPagingItems<Data>,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit
) {
    val itemsInOneRow: Int = when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 3
        WindowWidthSizeClass.Medium -> 4
        else -> 5
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(itemsInOneRow),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
    ) {
        items(
            count = animes.itemCount,
            key = { it },
        ) { index ->
//            val popularMovies = state.popularMovies[index]
            val anime = animes[index]
            AnimeItem(
                result = anime!!, modifier = Modifier.animateItemPlacement(), onClick = onClick
            )
            Log.d("HomeContent", "HomeContent: ${anime.images.jpg.image_url}")
        }

        animes.apply {
            when {
                loadState.refresh is LoadState.Loading -> { // first load
                    item(span = {
                        GridItemSpan(maxLineSpan)
                    }) { PaginationLoading(modifier.fillMaxWidth()) }
                }

                loadState.refresh is LoadState.Error -> {
                    val error = animes.loadState.refresh as LoadState.Error
                    item(span = {
                        GridItemSpan(maxLineSpan)
                    }) {
                        ErrorMessage(modifier = Modifier.fillMaxWidth(),
                            message = error.error.localizedMessage!!,
                            onClickRetry = { refresh() })
                    }
                }

                loadState.append is LoadState.Loading -> { // pagination
                    item(span = {
                        GridItemSpan(maxLineSpan)
                    }) { PaginationLoading(modifier.fillMaxWidth()) }
                }

                loadState.append is LoadState.Error -> {
                    val error = animes.loadState.append as LoadState.Error
                    item(span = {
                        GridItemSpan(maxLineSpan)
                    }) {
                        ErrorMessage(modifier = Modifier,
                            message = error.error.localizedMessage!!,
                            onClickRetry = { retry() })
                    }
                }
            }
        }
    }
}