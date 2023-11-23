package com.harissabil.anidex.ui.screen.auth.login

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
class LoginViewModel @Inject constructor(
    private val userPreference: UserPreference,
    private val repository: ProjekBasdatRepository
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow: SharedFlow<UIEvent> = _eventFlow.asSharedFlow()

    private val _state = mutableStateOf(LoginState())
    val state: State<LoginState> = _state

    var username = mutableStateOf("")
        private set

    var password = mutableStateOf("")
        private set

    fun onUsernameChange(username: String) {
        this.username.value = username
    }

    fun onPasswordChange(password: String) {
        this.password.value = password
    }

    private fun setUser(user: UserModel) {
        viewModelScope.launch {
            userPreference.saveUser(user)
        }
    }

    fun login() {
        viewModelScope.launch {
            val response = repository.login(username.value, password.value)
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
                            isLoginSuccess = false
                        )
                    }

                    is Resource.Loading -> {
                        _state.value = state.value.copy(isLoading = true, isLoginSuccess = false)
                    }

                    is Resource.Success -> {
                        val user = UserModel(
                            username = result.data?.data!![0].username,
                            password = result.data.data[0].password,
                            name = result.data.data[0].name,
                            email = result.data.data[0].email,
                            true
                        )
                        setUser(user)
                        _state.value = state.value.copy(
                            isLoading = false,
                            message = result.data.message,
                            isLoginSuccess = true
                        )
                    }
                }
            }
        }
    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
    }
}