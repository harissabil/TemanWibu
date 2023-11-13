package com.harissabil.anidex.data.remote.projekbasdat.dto.library

data class ReadLibraryResponse(
    val data: List<Data>?,
    val message: String,
    val status: String
)