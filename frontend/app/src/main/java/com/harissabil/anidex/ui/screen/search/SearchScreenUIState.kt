package com.harissabil.anidex.ui.screen.search

import androidx.paging.PagingData
import com.harissabil.anidex.data.remote.anime.dto.Data
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class SearchScreenUIState(
    val anime: Flow<PagingData<Data>> = flowOf(),
    val searchType: SearchType = SearchType.PLACEHOLDER,
    val isLoading: Boolean = false
)
