package com.harissabil.anidex.ui.screen.library

data class LibraryScreenUIState(
    val isLoading: Boolean = false,
    val libraryAnime: List<com.harissabil.anidex.data.remote.projekbasdat.dto.library.Data> = emptyList(),
    val reviewAnime: List<com.harissabil.anidex.data.remote.projekbasdat.dto.review.Data> = emptyList(),
)
