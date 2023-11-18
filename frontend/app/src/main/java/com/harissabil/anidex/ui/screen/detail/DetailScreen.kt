package com.harissabil.anidex.ui.screen.detail

import android.content.Intent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.harissabil.anidex.data.remote.anime.dto.Data
import com.harissabil.anidex.ui.components.detail.AddToButton
import com.harissabil.anidex.ui.components.detail.AddToLibraryDialog
import com.harissabil.anidex.ui.components.detail.AnimeGenre
import com.harissabil.anidex.ui.components.detail.AnimeHeader
import com.harissabil.anidex.ui.components.detail.AnimeSynopsis
import com.harissabil.anidex.ui.components.others.ErrorMessage
import com.harissabil.anidex.ui.components.others.PaginationLoading
import com.harissabil.anidex.ui.theme.spacing
import com.harissabil.anidex.util.Resource
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    malId: String,
    navigateBack: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val scrollState = rememberScrollState()
    val state = viewModel.state
    val isAddedToLibrary = viewModel.isAddedToLibrary
    val snackbarHostState = remember { SnackbarHostState() }

    var openDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = malId.toInt()) {
        viewModel.getAnimeById(malId.toInt())
        viewModel.checkIsAddedToLibrary(animeId = malId.toInt())
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is DetailViewModel.UIEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        text = "Anime Detail",
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navigateBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate up"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "https://myanimelist.net/anime/$malId"
                            )
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)
                        context.startActivity(shareIntent)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = "Share this Anime"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .animateContentSize(),
        ) {
            isAddedToLibrary.collectAsState().value.let { isAdded ->
                if (openDialog) {
                    AddToLibraryDialog(
                        onDismissRequest = { openDialog = false },
                        onConfirmation = {
                            if (viewModel.libraryStatus.value.isNotBlank()) {
                                viewModel.addOrEditLibrary()
                                openDialog = false
                            }
//                            val animeId = viewModel.state.value.data?.data?.mal_id ?: malId.toInt()
//                            viewModel.checkIsAddedToLibrary(animeId = animeId)
                        },
                        review = viewModel.review.value,
                        onReviewChange = viewModel::updateReview,
                        rating = viewModel.score.doubleValue.toFloat(),
                        onRatingChange = viewModel::updateScore,
                        onWishlistClick = { viewModel.updateLibraryStatus(AnimeStatus.WISHLIST.value) },
                        onWatchlistClick = { viewModel.updateLibraryStatus(AnimeStatus.WATCHLIST.value) },
                        onFinishedClick = { viewModel.updateLibraryStatus(AnimeStatus.FINISHED.value) },
                        status = viewModel.libraryStatus.value,
                        isAddedToLibrary = isAdded,
                    )
                }
                state.collectAsState().value.let { uiState ->
                    when (uiState) {
                        is Resource.Loading -> {
                            PaginationLoading(modifier = Modifier.fillMaxSize())
                        }

                        is Resource.Error -> {
                            ErrorMessage(
                                message = uiState.message.toString(),
                                onClickRetry = {
                                    viewModel.getAnimeById(malId.toInt())
                                },
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = MaterialTheme.spacing.medium)
                            )
                        }

                        is Resource.Success -> {
                            DetailContent(
                                anime = uiState.data!!.data,
                                onAddToLibraryClick = {
                                    openDialog = true
                                },
                                isAddedToLibrary = isAdded,
                                onDeleteClicked = {
                                    viewModel.deleteLibrary()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailContent(
    anime: Data,
    onAddToLibraryClick: () -> Unit,
    isAddedToLibrary: Boolean,
    onDeleteClicked: () -> Unit
) {
    AnimeHeader(
        malId = anime.mal_id,
        coverImages = anime.images.jpg.small_image_url,
        title = anime.title,
        titleJapanese = anime.title_japanese,
        rank = anime.rank,
        score = anime.score,
        scoredBy = anime.scored_by,
        status = anime.status
    )
    AddToButton(
        isAddedToLibrary = isAddedToLibrary,
        onAddToLibraryClick = onAddToLibraryClick,
        onDeleteClicked = onDeleteClicked
    )
    CardSection {
        AnimeInformation(anime = anime)
    }
    CardSection {
        AnimeGenre(anime = anime)
    }
    if (anime.synopsis != null) {
        CardSection {
            AnimeSynopsis(anime = anime)
        }
    }
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
}