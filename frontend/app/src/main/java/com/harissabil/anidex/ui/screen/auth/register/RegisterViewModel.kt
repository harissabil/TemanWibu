package com.harissabil.anidex.ui.screen.auth.register

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harissabil.anidex.data.local.UserPreference
import com.harissabil.anidex.data.local.model.UserModel
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
class RegisterViewModel @Inject constructor(
    private val userPreference: UserPreference,
    private val repository: ProjekBasdatRepository
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow: SharedFlow<UIEvent> = _eventFlow.asSharedFlow()

    private val _state = mutableStateOf(RegisterState())
    val state: State<RegisterState> = _state

    var name = mutableStateOf("")
        private set

    var username = mutableStateOf("")
        private set

    var email = mutableStateOf("")
        private set

    var password = mutableStateOf("")
        private set

    fun onNameChange(name: String) {
        this.name.value = name
    }

    fun onUsernameChange(username: String) {
        this.username.value = username
    }

    fun onEmailChange(email: String) {
        this.email.value = email
    }

    fun onPasswordChange(password: String) {
        this.password.value = password
    }

    private fun setUser(user: UserModel) {
        viewModelScope.launch {
            userPreference.saveUser(user)
        }
    }

    fun register() {
        viewModelScope.launch {
            val response =
                repository.register(username.value, password.value, name.value, email.value)
            response.collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _eventFlow.emit(
                            UIEvent.ShowSnackbar(
                                result.data?.message ?: "Unknown error"
                            )
                        )
                        _state.value = state.value.copy(
                            isLoading = false,
                            message = result.data?.message ?: "Unknown error",
                            isRegisterSuccess = false
                        )
                    }

                    is Resource.Loading -> {
                        _state.value = state.value.copy(isLoading = true, isRegisterSuccess = false)
                    }

                    is Resource.Success -> {
                        if (result.data!!.status == "success") {
                            val user = UserModel(
                                username = username.value,
                                password = password.value,
                                name = name.value,
                                email = email.value,
                                true
                            )
                            setUser(user)
                            _state.value = state.value.copy(
                                isLoading = false,
                                message = result.data.message,
                                isRegisterSuccess = true
                            )
                        } else if (result.data.status == "error") {
                            _eventFlow.emit(
                                UIEvent.ShowSnackbar(
                                    result.data.message
                                )
                            )
                            _state.value = state.value.copy(
                                isLoading = false,
                                message = result.data.message,
                                isRegisterSuccess = false
                            )
                        }
                    }
                }
            }
        }
    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
    }
}