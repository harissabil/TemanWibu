package com.harissabil.anidex.ui.screen.library

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harissabil.anidex.data.local.UserPreference
import com.harissabil.anidex.data.repository.ProjekBasdatRepository
import com.harissabil.anidex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val userPreference: UserPreference,
    private val repository: ProjekBasdatRepository,
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow: SharedFlow<UIEvent> = _eventFlow.asSharedFlow()

    private val _state = mutableStateOf(LibraryScreenUIState())
    val state: State<LibraryScreenUIState> = _state

    private var usernamePref = mutableStateOf("")

    private fun getUser() {
        viewModelScope.launch {
            userPreference.getUser().collectLatest { user ->
                usernamePref.value = user.username
            }
        }
    }

    fun readLibrary(
        username: String = usernamePref.value
    ) {
        viewModelScope.launch {
            repository.readLibrary(username).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        _eventFlow.emit(
                            UIEvent.ShowSnackbar(
                                result.message ?: "Unknown error"
                            )
                        )
                        _state.value = state.value.copy(
                            isLoading = false,
                        )
                    }

                    is Resource.Loading -> _state.value = state.value.copy(
                        isLoading = true,
                    )

                    is Resource.Success -> _state.value = state.value.copy(
                        isLoading = false,
                        libraryAnime = result.data?.data ?: emptyList(),
                    )
                }
            }
        }
    }

    fun readReview(
        username: String = usernamePref.value
    ) {
        viewModelScope.launch {
            repository.readReview(username).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        _eventFlow.emit(
                            UIEvent.ShowSnackbar(
                                result.message ?: "Unknown error"
                            )
                        )
                        _state.value = state.value.copy(
                            isLoading = false,
                        )
                    }

                    is Resource.Loading -> _state.value = state.value.copy(
                        isLoading = true,
                    )

                    is Resource.Success -> _state.value = state.value.copy(
                        isLoading = false,
                        reviewAnime = result.data?.data ?: emptyList(),
                    )
                }
            }
        }
    }

    fun deleteReview(
        reviewId: Int
    ) {
        viewModelScope.launch {
            val library = repository.deleteReview(reviewId)
            library.collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        _eventFlow.emit(UIEvent.ShowSnackbar(result.data?.message.toString()))
                    }

                    is Resource.Error -> {
                        _eventFlow.emit(UIEvent.ShowSnackbar(result.data?.message ?: result.message ?: "Oops, something went wrong."))
                    }

                    is Resource.Loading -> {
                        _state.value = state.value.copy(isLoading = true)
                    }
                }
            }
        }
        readReview()
    }

    init {
        getUser()
        viewModelScope.launch {
            readLibrary()
            readReview()
        }
    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
    }
}