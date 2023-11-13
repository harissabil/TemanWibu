package com.harissabil.anidex.ui.screen.forum

import com.harissabil.anidex.data.remote.projekbasdat.dto.review.AllReviewData

data class ForumScreenUIState(
    val isLoading: Boolean = false,
    val allReviewedAnime: List<AllReviewData> = emptyList(),
)
