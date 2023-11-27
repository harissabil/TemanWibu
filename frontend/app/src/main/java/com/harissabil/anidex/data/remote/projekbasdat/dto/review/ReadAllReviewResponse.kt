package com.harissabil.anidex.data.remote.projekbasdat.dto.review

data class ReadAllReviewResponse(
    val data: List<AllReviewData>?,
    val message: String,
    val status: String
)

data class AllReviewData(
    val anime_id: String,
    val anime_review: String,
    val anime_score: String,
    val review_date: String,
    val episode: String,
    val poster_image: String,
    val rating: String,
    val review_id: Int,
    val title: String,
    val username: String
)