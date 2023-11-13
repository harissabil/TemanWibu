package com.harissabil.anidex.ui.screen.detail

import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harissabil.anidex.data.local.UserPreference
import com.harissabil.anidex.data.remote.anime.dto.AnimeByIdResponse
import com.harissabil.anidex.data.repository.AnimeRepository
import com.harissabil.anidex.data.repository.ProjekBasdatRepository
import com.harissabil.anidex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val userPreference: UserPreference,
    private val repository: AnimeRepository,
    private val repositoryBackend: ProjekBasdatRepository,
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow: SharedFlow<UIEvent> = _eventFlow.asSharedFlow()

    private val _state: MutableStateFlow<Resource<AnimeByIdResponse>> =
        MutableStateFlow(Resource.Loading())
    val state: StateFlow<Resource<AnimeByIdResponse>> = _state.asStateFlow()

    private var usernamePref = mutableStateOf("")

    private val _isAddedToLibrary = MutableStateFlow(false)
    val isAddedToLibrary: StateFlow<Boolean> = _isAddedToLibrary.asStateFlow()

    private val _libraryStatus = mutableStateOf("")
    val libraryStatus = _libraryStatus

    private val _review = mutableStateOf("")
    val review = _review

    private val _score = mutableDoubleStateOf(0.0)
    val score = _score

    private fun getUser() {
        viewModelScope.launch {
            userPreference.getUser().collectLatest { user ->
                usernamePref.value = user.username
            }
        }
    }

    fun updateLibraryStatus(newStatus: String) {
        _libraryStatus.value = newStatus
    }

    fun updateReview(newReview: String) {
        _review.value = newReview
    }

    fun updateScore(newScore: Float) {
        _score.doubleValue = newScore.toDouble()
    }

    fun checkIsAddedToLibrary(
        username: String = usernamePref.value,
        animeId: Int = state.value.data!!.data.mal_id,
    ) = viewModelScope.launch {
        val library = repositoryBackend.checkLibrary(username, animeId)
        library.collectLatest { result ->
            when (result) {
                is Resource.Success -> {
                    _isAddedToLibrary.value = result.data?.available ?: false
                    _libraryStatus.value = result.data?.anime_status ?: ""
                    if (!result.data?.review_data.isNullOrEmpty()) {
                        _review.value = result.data?.review_data?.get(0)?.anime_review ?: ""
                        _score.doubleValue =
                            result.data?.review_data?.get(0)?.anime_score?.toDouble() ?: 0.0
                    } else {
                        _review.value = ""
                        _score.doubleValue = 0.0
                    }
                }

                is Resource.Error -> {
                    _eventFlow.emit(UIEvent.ShowSnackbar(result.message.toString()))
                    _isAddedToLibrary.value = result.data?.available ?: false
                    _libraryStatus.value = result.data?.anime_status ?: ""
                    if (!result.data?.review_data.isNullOrEmpty()) {
                        _review.value = result.data?.review_data?.get(0)?.anime_review ?: ""
                        _score.doubleValue =
                            result.data?.review_data?.get(0)?.anime_score?.toDouble() ?: 0.0
                    } else {
                        _review.value = ""
                        _score.doubleValue = 0.0
                    }
                }

                is Resource.Loading -> {
                    _isAddedToLibrary.value = result.data?.available ?: false
                    _libraryStatus.value = result.data?.anime_status ?: ""
                }
            }
        }
    }

    fun getAnimeById(id: Int) = viewModelScope.launch {
        val anime = repository.getAnimeById(id)
        anime.collectLatest { result ->
            _state.value = result
        }
    }

    fun addOrEditLibrary() {
        if (isAddedToLibrary.value) {
            if (review.value.isNotBlank()) {
                updateLibrary(
                    animeReview = review.value,
                    animeScore = score.doubleValue,
                    currentStatus = libraryStatus.value
                )
            } else {
                updateLibraryWithoutReview(
                    currentStatus = libraryStatus.value
                )
            }
        } else {
            if (review.value.isNotBlank()) {
                createLibrary(
                    animeReview = review.value,
                    animeScore = score.doubleValue,
                    currentStatus = libraryStatus.value
                )
            } else {
                createLibraryWithoutReview(
                    currentStatus = libraryStatus.value
                )
            }
        }
        _isAddedToLibrary.value = true
    }

    private fun createLibrary(
        username: String = usernamePref.value,
        animeId: Int = state.value.data!!.data.mal_id,
        currentStatus: String,
        title: String = state.value.data!!.data.title.replace("'", "''"),
        posterImage: String = state.value.data!!.data.images.jpg.image_url,
        rating: Double = if (state.value.data?.data?.score == null) 0.0 else state.value.data?.data?.score!!,
        episode: Int = if (state.value.data?.data?.episodes == null) 0 else state.value.data?.data?.episodes!!,
        animeReview: String,
        animeScore: Double,
    ) = viewModelScope.launch {
        val library = repositoryBackend.createLibrary(
            username,
            animeId,
            currentStatus,
            title,
            posterImage,
            rating,
            episode,
            animeReview,
            animeScore
        )
        library.collectLatest { result ->
            when (result) {
                is Resource.Success -> {
                    _eventFlow.emit(UIEvent.ShowSnackbar(result.data?.message.toString()))
                }

                is Resource.Error -> {
                    _eventFlow.emit(UIEvent.ShowSnackbar(result.data?.message.toString()))
                }

                is Resource.Loading -> {

                }
            }
        }
    }

    private fun createLibraryWithoutReview(
        username: String = usernamePref.value,
        animeId: Int = state.value.data!!.data.mal_id,
        currentStatus: String,
        title: String = state.value.data!!.data.title.replace("'", "''"),
        posterImage: String = state.value.data!!.data.images.jpg.image_url,
        rating: Double = if (state.value.data?.data?.score == null) 0.0 else state.value.data?.data?.score!!,
        episode: Int = if (state.value.data?.data?.episodes == null) 0 else state.value.data?.data?.episodes!!,
    ) = viewModelScope.launch {
        val library = repositoryBackend.createLibraryWithoutReview(
            username,
            animeId,
            currentStatus,
            title,
            posterImage,
            rating,
            episode,
        )
        library.collectLatest { result ->
            when (result) {
                is Resource.Success -> {
                    _eventFlow.emit(UIEvent.ShowSnackbar(result.data?.message.toString()))
                }

                is Resource.Error -> {
                    _eventFlow.emit(UIEvent.ShowSnackbar(result.data?.message.toString()))
                }

                is Resource.Loading -> {

                }
            }
        }
    }

    private fun updateLibrary(
        username: String = usernamePref.value,
        animeId: Int = state.value.data!!.data.mal_id,
        currentStatus: String,
        title: String = state.value.data!!.data.title.replace("'", "''"),
        posterImage: String = state.value.data!!.data.images.jpg.image_url,
        rating: Double = if (state.value.data?.data?.score == null) 0.0 else state.value.data?.data?.score!!,
        episode: Int = if (state.value.data?.data?.episodes == null) 0 else state.value.data?.data?.episodes!!,
        animeReview: String,
        animeScore: Double,
    ) = viewModelScope.launch {
        val library = repositoryBackend.updateLibrary(
            username,
            animeId,
            currentStatus,
            title,
            posterImage,
            rating,
            episode,
            animeReview,
            animeScore
        )
        library.collectLatest { result ->
            when (result) {
                is Resource.Success -> {
                    _eventFlow.emit(UIEvent.ShowSnackbar(result.data?.message.toString()))
                }

                is Resource.Error -> {
                    _eventFlow.emit(UIEvent.ShowSnackbar(result.data?.message.toString()))
                }

                is Resource.Loading -> {

                }
            }
        }
    }

    private fun updateLibraryWithoutReview(
        username: String = usernamePref.value,
        animeId: Int = state.value.data!!.data.mal_id,
        currentStatus: String,
        title: String = state.value.data!!.data.title.replace("'", "''"),
        posterImage: String = state.value.data!!.data.images.jpg.image_url,
        rating: Double = if (state.value.data?.data?.score == null) 0.0 else state.value.data?.data?.score!!,
        episode: Int = if (state.value.data?.data?.episodes == null) 0 else state.value.data?.data?.episodes!!,
    ) = viewModelScope.launch {
        val library = repositoryBackend.updateLibraryWithoutReview(
            username,
            animeId,
            currentStatus,
            title,
            posterImage,
            rating,
            episode,
        )
        library.collectLatest { result ->
            when (result) {
                is Resource.Success -> {
                    _eventFlow.emit(UIEvent.ShowSnackbar(result.data?.message.toString()))
                }

                is Resource.Error -> {
                    _eventFlow.emit(UIEvent.ShowSnackbar(result.data?.message.toString()))
                }

                is Resource.Loading -> {

                }
            }
        }
    }

    fun deleteLibrary(
        username: String = usernamePref.value,
        animeId: Int = state.value.data!!.data.mal_id,
    ) {
        viewModelScope.launch {
            val library = repositoryBackend.deleteLibrary(username, animeId)
            library.collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        _eventFlow.emit(UIEvent.ShowSnackbar(result.data?.message.toString()))
                    }

                    is Resource.Error -> {
                        _eventFlow.emit(UIEvent.ShowSnackbar(result.data?.message.toString()))
                    }

                    is Resource.Loading -> {

                    }
                }
            }
        }
        _isAddedToLibrary.value = false
    }

    init {
        getUser()
    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
    }
}