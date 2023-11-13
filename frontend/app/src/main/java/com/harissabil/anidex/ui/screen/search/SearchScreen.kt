package com.harissabil.anidex.ui.screen.search

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.harissabil.anidex.data.remote.anime.dto.Data
import com.harissabil.anidex.ui.components.items.AnimeList
import com.harissabil.anidex.ui.components.others.ErrorMessage
import com.harissabil.anidex.ui.components.others.PaginationLoading
import com.harissabil.anidex.ui.components.others.ScrollToTopButton
import com.harissabil.anidex.ui.components.search.Search
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    navigateToDetail: (String) -> Unit
) {
    val query = viewModel.searchQuery.collectAsState().value
    val scope = rememberCoroutineScope()

    viewModel.state.collectAsState().value.let { state ->
        val listOfLazyListState: Map<SearchType, LazyListState> = mapOf(
            Pair(SearchType.PLACEHOLDER, rememberLazyListState()),
            Pair(SearchType.REAL, rememberLazyListState()),
        )
        val lazyListState = listOfLazyListState[state.searchType] ?: rememberLazyListState()
        val showButton: Boolean by remember(lazyListState) {
            derivedStateOf { lazyListState.firstVisibleItemIndex > 0 }
        }
        Scaffold(
            topBar = {
                Search(
                    query = query,
                    active = false,
                    onQueryChange = viewModel::onQueryChange,
                    onActiveChange = {},
                    onCloseIcon = { /*TODO*/ },
                    onSearch = {
                        viewModel.onEnterSearch(it)
                        scope.launch {
                            lazyListState.animateScrollToItem(0)
                        }
                    },
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            },
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.BottomCenter
            ) {
                val anime = state.anime.collectAsLazyPagingItems()
                SearchContent(
                    listState = lazyListState,
                    animes = anime,
                    navigateToDetail = navigateToDetail
                )
                AnimatedVisibility(
                    visible = showButton,
                    enter = fadeIn() + slideInVertically(
                        initialOffsetY = { fullHeight -> fullHeight }
                    ),
                    exit = fadeOut() + slideOutVertically(
                        targetOffsetY = { fullHeight -> fullHeight }
                    ),
                    modifier = Modifier
                        .padding(bottom = 30.dp, end = 16.dp)
                        .align(Alignment.BottomEnd)
                ) {
                    ScrollToTopButton(
                        onClick = {
                            scope.launch {
                                lazyListState.animateScrollToItem(index = 0)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SearchContent(
    listState: LazyListState,
    animes: LazyPagingItems<Data>,
    modifier: Modifier = Modifier,
    navigateToDetail: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
    ) {
        items(
            count = animes.itemCount,
            key = { it },
        ) { index ->
            val anime = animes[index]
            AnimeList(anime = anime!!, onAnimeClicked = navigateToDetail)
            HorizontalDivider()
            Log.d("HomeContent", "HomeContent: ${anime.images.jpg.image_url}")
        }

        animes.apply {
            when {
                loadState.refresh is LoadState.Loading -> { // first load
                    item { PaginationLoading(modifier.fillMaxWidth()) }
                }

                loadState.refresh is LoadState.Error -> {
                    val error = animes.loadState.refresh as LoadState.Error
                    item {
                        ErrorMessage(
                            modifier = Modifier.fillMaxWidth(),
                            message = error.error.localizedMessage!!,
                            onClickRetry = { refresh() }
                        )
                    }
                }

                loadState.append is LoadState.Loading -> { // pagination
                    item { PaginationLoading(modifier.fillMaxWidth()) }
                }

                loadState.append is LoadState.Error -> {
                    val error = animes.loadState.append as LoadState.Error
                    item {
                        ErrorMessage(
                            modifier = Modifier,
                            message = error.error.localizedMessage!!,
                            onClickRetry = { retry() })
                    }
                }
            }
        }
    }
}

enum class SearchType {
    PLACEHOLDER, REAL
}