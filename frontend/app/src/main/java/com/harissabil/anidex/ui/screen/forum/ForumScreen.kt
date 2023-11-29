package com.harissabil.anidex.ui.screen.forum

import androidx.compose.animation.animateContentSize
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.harissabil.anidex.ui.components.review.AnimeAllReviewListItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ForumScreen(
    viewModel: ForumViewModel = hiltViewModel(),
    navigateToDetail: (String) -> Unit
) {
    val state by viewModel.state
    val username by viewModel.usernamePref
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val allReviewPullRefreshState = rememberPullRefreshState(
        refreshing = state.isLoading,
        onRefresh = viewModel::readAllReview
    )
    val newItemAdded: Boolean by remember(lazyListState) {
        derivedStateOf { lazyListState.firstVisibleItemIndex <= 1 }
    }

    if (newItemAdded) {
        LaunchedEffect(key1 = state.isLoading) {
            scope.launch {
                lazyListState.animateScrollToItem(0)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
        ) {
            ForumContent(
                forumScreenUIState = state,
                listState = lazyListState,
                allReviewPullRefreshState = allReviewPullRefreshState,
                navigateToDetail = navigateToDetail,
                onReviewDeleted = viewModel::deleteReview,
                username = username,
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ForumContent(
    forumScreenUIState: ForumScreenUIState,
    listState: LazyListState,
    allReviewPullRefreshState: PullRefreshState,
    navigateToDetail: (String) -> Unit,
    onReviewDeleted: (Int) -> Unit,
    username: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier.fillMaxSize()) {

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
        val reviews = forumScreenUIState.allReviewedAnime
        if (reviews.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(allReviewPullRefreshState)
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
                    text = "No Reviews Found",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 128.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        } else if (reviews.size <= 3) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(allReviewPullRefreshState)
                    .verticalScroll(rememberScrollState())
                    .animateContentSize()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .then(modifier),
            ) {
                reviews.indices.forEach { index ->
                    AnimeAllReviewListItem(
                        anime = reviews[index],
                        onAnimeClicked = navigateToDetail,
                        onReviewDeleted = onReviewDeleted,
                        username = username,
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
                    .pullRefresh(allReviewPullRefreshState)
                    .animateContentSize()
                    .then(modifier),
            ) {
                items(
                    count = reviews.size,
                    key = { it },
                ) { index ->
                    val anime = reviews[index]
                    AnimeAllReviewListItem(
                        anime = anime,
                        onAnimeClicked = navigateToDetail,
                        onReviewDeleted = onReviewDeleted,
                        username = username,
                    )
                    HorizontalDivider()
                }
            }
        }

        PullRefreshIndicator(
            refreshing = forumScreenUIState.isLoading,
            state = allReviewPullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.primary,
        )
    }
}