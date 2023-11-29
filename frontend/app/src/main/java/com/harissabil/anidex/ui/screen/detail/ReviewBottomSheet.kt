package com.harissabil.anidex.ui.screen.detail

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.harissabil.anidex.R
import com.harissabil.anidex.data.remote.projekbasdat.dto.review.ReadAnimeReviewResponse
import com.harissabil.anidex.ui.components.items.AnimeReviewItem
import com.harissabil.anidex.ui.components.others.ErrorMessage
import com.harissabil.anidex.util.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewBottomSheet(
    onDismissRequest: () -> Unit,
    onClickRetry: () -> Unit,
    sheetState: SheetState,
    listState: LazyListState,
    reviews: Resource<ReadAnimeReviewResponse>,
    modifier: Modifier = Modifier,
) {
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

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "Reviews",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)
            )
            HorizontalDivider()
            when (reviews) {
                is Resource.Error -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        ErrorMessage(
                            message = reviews.data?.message ?: reviews.message
                            ?: "Oops, something went wrong.",
                            onClickRetry = onClickRetry,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                is Resource.Loading -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    }
                }

                is Resource.Success -> {
                    if (reviews.data?.data!!.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
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
                                text = reviews.data.message,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 128.dp),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    } else if (reviews.data.data.size <= 3) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .animateContentSize()
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                                .then(modifier),
                        ) {
                            reviews.data.data.indices.forEach { index ->
                                val review = reviews.data.data[index]
                                AnimeReviewItem(
                                    anime = review,
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
                                .animateContentSize()
                                .then(modifier),
                        ) {
                            items(
                                count = reviews.data.data.size,
                                key = { it },
                            ) { index ->
                                val review = reviews.data.data[index]
                                AnimeReviewItem(
                                    anime = review,
                                )
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }
        }
    }
}