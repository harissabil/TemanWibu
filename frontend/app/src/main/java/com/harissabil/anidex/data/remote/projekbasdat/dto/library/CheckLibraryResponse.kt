package com.harissabil.anidex.data.remote.projekbasdat.dto.library

data class CheckLibraryResponse(
    val anime_status: String,
    val available: Boolean,
    val message: String,
    val review_data: List<ReviewData>?,
    val status: String
)