package com.harissabil.anidex.data.remote.projekbasdat.dto.review

data class ReadAnimeReviewResponse(
	val data: List<AllReviewData>?,
	val message: String,
	val status: String
)

