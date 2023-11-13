package com.harissabil.anidex.data.remote.projekbasdat.dto.library

data class Data(
    val library_id: Int,
    val anime_id: String,
    val current_status: String,
    val episode: String,
    val poster_image: String,
    val rating: String,
    val title: String,
    val username: String
)