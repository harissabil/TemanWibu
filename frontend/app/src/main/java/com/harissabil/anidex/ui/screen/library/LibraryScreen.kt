package com.harissabil.anidex.ui.screen.library

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Down
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Up
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.harissabil.anidex.R
import com.harissabil.anidex.ui.components.button.FilterFab
import com.harissabil.anidex.ui.components.library.AnimeLibraryListItem
import com.harissabil.anidex.ui.components.library.AnimeReviewListItem
import com.harissabil.anidex.ui.components.library.LibraryTabs
import com.leinardi.android.speeddial.compose.SpeedDialOverlay
import com.leinardi.android.speeddial.compose.SpeedDialState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalFoundationApi::class, ExperimentalMaterialApi::class
)
@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = hiltViewModel(),
    navigateToDetail: (String) -> Unit
) {
    val state = viewModel.state.value
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    val pagerState = rememberPagerState {
        2
    }
    val listOfLazyListState: Map<Int, LazyListState> = mapOf(
        Pair(0, rememberLazyListState()),
        Pair(1, rememberLazyListState()),
    )
    val lazyListState = listOfLazyListState[selectedIndex] ?: rememberLazyListState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val libraryPullRefreshState = rememberPullRefreshState(
        refreshing = state.isLoading,
        onRefresh = viewModel::readLibrary
    )
    val reviewPullRefreshState = rememberPullRefreshState(
        refreshing = state.isLoading,
        onRefresh = viewModel::readReview
    )
    var speedDialState by rememberSaveable { mutableStateOf(SpeedDialState.Collapsed) }
    var overlayVisible: Boolean by rememberSaveable { mutableStateOf(speedDialState.isExpanded()) }
    val newItemAdded: Boolean by remember(lazyListState) {
        derivedStateOf { lazyListState.firstVisibleItemIndex <= 1 }
    }

    if (newItemAdded) {
        //scroll to top to ensure latest added book gets visible
        LaunchedEffect(key1 = state.isLoading) {
            scope.launch {
                lazyListState.animateScrollToItem(0)
            }
        }
    }
    LaunchedEffect(key1 = selectedIndex) {
        pagerState.animateScrollToPage(selectedIndex)
        if (selectedIndex == 0) {
            viewModel.readLibrary()
        } else {
            viewModel.readReview()
        }
    }
    LaunchedEffect(key1 = pagerState.currentPage) {
        selectedIndex = pagerState.currentPage
    }
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is LibraryViewModel.UIEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    fun onFabItemClick(filterType: FilterType) {
        overlayVisible = false
        speedDialState = speedDialState.toggle()
        viewModel.updateFilterType(filterType)
        scope.launch {
            lazyListState.animateScrollToItem(0)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            if (selectedIndex == 0) {
                FilterFab(
                    speedDialState = speedDialState,
                    onFabClick = { expanded ->
                        overlayVisible = !expanded
                        speedDialState = speedDialState.toggle()
                    },
                    onNoneClick = { onFabItemClick(it) },
                    onWishlistClick = { onFabItemClick(it) },
                    onWatchlistClick = { onFabItemClick(it) },
                    onFinishedClick = { onFabItemClick(it) },
                    listState = lazyListState,
                )
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
        ) {
            LibraryTabs(
                selectedIndex = selectedIndex,
                onTabSelected = { index ->
                    selectedIndex = index
                },
            )
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { index ->
                LibraryContent(
                    tabSelected = index,
                    libraryScreenUIState = state,
                    listState = lazyListState,
                    libraryPullRefreshState = libraryPullRefreshState,
                    reviewPullRefreshState = reviewPullRefreshState,
                    navigateToDetail = navigateToDetail,
                    onReviewDeleted = viewModel::deleteReview
                )
            }
        }
        SpeedDialOverlay(
            visible = overlayVisible,
            onClick = {
                overlayVisible = false
                speedDialState = speedDialState.toggle()
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LibraryContent(
    tabSelected: Int,
    libraryScreenUIState: LibraryScreenUIState,
    listState: LazyListState,
    libraryPullRefreshState: PullRefreshState,
    reviewPullRefreshState: PullRefreshState,
    navigateToDetail: (String) -> Unit,
    onReviewDeleted: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        targetState = tabSelected,
        label = "",
        transitionSpec = {
            slideIntoContainer(
                animationSpec = tween(300, easing = EaseIn),
                towards = Up
            ).togetherWith(
                slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = Down
                )
            )
        }
    ) { targetTab ->
        Box(modifier = modifier.fillMaxSize()) {
            when (targetTab) {
                0 -> {
                    val lottieEmptyAnimation by rememberLottieComposition(
                        spec = LottieCompositionSpec.RawRes(
                            R.raw.ghibli_girl_animation
                        )
                    )
                    val isLottiePlaying by remember { mutableStateOf(true) }
                    val lottieProgress by animateLottieCompositionAsState(
                        composition = lottieEmptyAnimation,
                        iterations = LottieConstants.IterateForever,
                        isPlaying = isLottiePlaying
                    )
                    val animes = libraryScreenUIState.libraryAnime
                    if (animes.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .pullRefresh(libraryPullRefreshState)
                                .verticalScroll(rememberScrollState())
                                .padding(64.dp)
                                .then(modifier),
                            contentAlignment = Alignment.Center,
                        ) {
                            LottieAnimation(
                                composition = lottieEmptyAnimation,
                                progress = { lottieProgress },
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                            Text(
                                text = "Your library is empty!",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 128.dp),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    } else if (animes.size <= 3) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .pullRefresh(libraryPullRefreshState)
                                .verticalScroll(rememberScrollState())
                                .animateContentSize()
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                                .then(modifier),
                        ) {
                            animes.indices.forEach { index ->
                                AnimeLibraryListItem(
                                    anime = animes[index],
                                    onAnimeClicked = navigateToDetail
                                )
                                HorizontalDivider()
                            }
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                            state = listState,
                            modifier = Modifier
                                .fillMaxSize()
                                .pullRefresh(libraryPullRefreshState)
                                .animateContentSize()
                                .then(modifier),
                        ) {
                            items(
                                count = animes.size,
                                key = { it },
                            ) { index ->
                                val anime = animes[index]
                                AnimeLibraryListItem(
                                    anime = anime,
                                    onAnimeClicked = navigateToDetail
                                )
                                HorizontalDivider()
                            }
                        }
                    }

                    PullRefreshIndicator(
                        refreshing = libraryScreenUIState.isLoading,
                        state = libraryPullRefreshState,
                        modifier = Modifier.align(Alignment.TopCenter),
                        backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                        contentColor = MaterialTheme.colorScheme.primary,
                    )
                }

                1 -> {
                    val lottieEmptyAnimation by rememberLottieComposition(
                        spec = LottieCompositionSpec.RawRes(
                            R.raw.ghibli_girl_animation
                        )
                    )
                    val isLottiePlaying by remember { mutableStateOf(true) }
                    val lottieProgress by animateLottieCompositionAsState(
                        composition = lottieEmptyAnimation,
                        iterations = LottieConstants.IterateForever,
                        isPlaying = isLottiePlaying
                    )
                    val animes = libraryScreenUIState.reviewAnime
                    if (animes.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .pullRefresh(reviewPullRefreshState)
                                .verticalScroll(rememberScrollState())
                                .padding(64.dp)
                                .then(modifier),
                            contentAlignment = Alignment.Center,
                        ) {
                            LottieAnimation(
                                composition = lottieEmptyAnimation,
                                progress = { lottieProgress },
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                            Text(
                                text = "You haven't reviewed any anime yet!",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 148.dp),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    } else if (animes.size <= 3) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .pullRefresh(reviewPullRefreshState)
                                .verticalScroll(rememberScrollState())
                                .animateContentSize()
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                                .then(modifier),
                        ) {
                            animes.indices.forEach { index ->
                                AnimeReviewListItem(
                                    anime = animes[index],
                                    onAnimeClicked = navigateToDetail,
                                    onReviewDeleted = onReviewDeleted
                                )
                                HorizontalDivider()
                            }
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                            state = listState,
                            modifier = Modifier
                                .fillMaxSize()
                                .pullRefresh(reviewPullRefreshState)
                                .animateContentSize()
                                .then(modifier),
                        ) {
                            items(
                                count = animes.size,
                                key = { it },
                            ) { index ->
                                val anime = animes[index]
                                AnimeReviewListItem(
                                    anime = anime,
                                    onAnimeClicked = navigateToDetail,
                                    onReviewDeleted = onReviewDeleted
                                )
                                HorizontalDivider()
                            }
                        }
                    }

                    PullRefreshIndicator(
                        refreshing = libraryScreenUIState.isLoading,
                        state = reviewPullRefreshState,
                        modifier = Modifier.align(Alignment.TopCenter),
                        backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
                        contentColor = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}