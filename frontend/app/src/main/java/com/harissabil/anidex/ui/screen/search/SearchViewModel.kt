package com.harissabil.anidex.ui.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.harissabil.anidex.data.remote.anime.dto.Data
import com.harissabil.anidex.data.repository.AnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: AnimeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SearchScreenUIState())
    val state: StateFlow<SearchScreenUIState> = _state.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    private val searchQueryToPaging = MutableStateFlow("")

    fun onQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    private fun onFirstSearch() {
        _state.value = state.value.copy(
            anime = repository.getPopularAnime().cachedIn(viewModelScope)
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val searchedAnime: Flow<PagingData<Data>> = searchQueryToPaging
        .filterNotNull() // Skip our default value
        .distinctUntilChanged() // Don't return a new value if the category hasn't changed
        .flatMapLatest { query ->
            repository.getAnimeSearch(query)
        }.cachedIn(viewModelScope)

    fun onEnterSearch(query: String) {
        _searchQuery.value = query
        searchQueryToPaging.value = query
        _state.value = state.value.copy(
            anime = searchedAnime,
            searchType = SearchType.REAL
        )
    }

//    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
//    private val searchedAnime: Flow<PagingData<Data>> = searchQuery
//        .debounce(0)
//        .distinctUntilChanged()
//        .flatMapLatest {
//            repository.getAnimeSearch(
//                query = it
//            )
//        }
//        .cachedIn(viewModelScope)
//
//    val searchScreenState: StateFlow<SearchScreenUIState> =
//        searchedAnime.map {
//            SearchScreenUIState(
//                anime = searchedAnime
//            )
//        }.stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.Eagerly,
//            initialValue = SearchScreenUIState()
//        )

    init {
        onFirstSearch()
    }

}