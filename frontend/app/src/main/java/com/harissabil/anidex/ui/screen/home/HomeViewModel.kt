package com.harissabil.anidex.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.harissabil.anidex.data.remote.anime.dto.Data
import com.harissabil.anidex.data.repository.AnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
//    private val repository: MovieRepository,
    repository: AnimeRepository
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow: SharedFlow<UIEvent> = _eventFlow.asSharedFlow()

    private val displayedAnimeType: MutableStateFlow<DisplayType> =
        MutableStateFlow(DisplayType.POPULAR)

    private val popularAnime: Flow<PagingData<Data>> =
        repository.getPopularAnime().cachedIn(viewModelScope)

    private val airingAnime: Flow<PagingData<Data>> =
        repository.getSeasonNowAnime().cachedIn(viewModelScope)

    private val upcomingAnime: Flow<PagingData<Data>> =
        repository.getSeasonUpcomingAnime().cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val anime: Flow<PagingData<Data>> =
        displayedAnimeType.flatMapLatest { type ->
            when (type) {
                DisplayType.POPULAR -> popularAnime
                DisplayType.AIRING -> airingAnime
                DisplayType.UPCOMING -> upcomingAnime
                DisplayType.SEARCH -> popularAnime
            }
        }.cachedIn(viewModelScope)

    val homeScreenState: StateFlow<HomeScreenUIState> =
        displayedAnimeType.map {
            HomeScreenUIState(
                displayType = it,
                anime = anime
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = HomeScreenUIState()
        )

    fun updateDisplayType(displayState: DisplayType) {
        displayedAnimeType.value = displayState
    }

//    private fun mGetPopularMovies() {
//        repository.getPopularMovies().onEach { result ->
//            when (result) {
//                is Resource.Success -> {
//                    _state.value = state.value.copy(
//                        popularMovies = result.data ?: emptyList(),
//                        isLoading = false
//                    )
//                    Log.d("HomeViewModel", "mGetPopularMovies: ${result.data}")
//                }
//
//                is Resource.Loading -> {
//                    _state.value = state.value.copy(
//                        popularMovies = result.data ?: emptyList(),
//                        isLoading = true
//                    )
//                }
//
//                is Resource.Error -> {
//                    _state.value = state.value.copy(
//                        popularMovies = result.data ?: emptyList(),
//                        isLoading = false
//                    )
//                    _eventFlow.emit(
//                        UIEvent.ShowSnackbar(
//                            result.message ?: "Unknown Error"
//                        )
//                    )
//                }
//            }
//        }.launchIn(viewModelScope)
//    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
    }
}