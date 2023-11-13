package com.harissabil.anidex.ui.screen.home

import androidx.paging.PagingData
import com.harissabil.anidex.data.remote.anime.dto.Data
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class HomeScreenUIState(
    val popularAnime: List<Data> = emptyList(),
    val displayType: DisplayType = DisplayType.POPULAR,
    val isLoading: Boolean = false,
    val anime: Flow<PagingData<Data>> = flowOf()
)