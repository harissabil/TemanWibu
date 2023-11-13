package com.harissabil.anidex.ui.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harissabil.anidex.data.local.UserPreference
import com.harissabil.anidex.data.local.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userPreference: UserPreference
) : ViewModel() {

//    val userState : StateFlow<UserModel> = userPreference.getUser().stateIn(
//        scope = viewModelScope,
//        started = WhileSubscribed(),
//        initialValue = UserModel("", "", "", "", false)
//    )

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _userState = MutableStateFlow(UserModel("", "", "", "", false))
    val userState: StateFlow<UserModel> = _userState.asStateFlow()

    private fun getUser() {
        viewModelScope.launch {
            userPreference.getUser().collectLatest { user ->
                _userState.value = user
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPreference.logout()
            getUser()
        }
    }

    init {
        viewModelScope.launch {
            getUser()
            delay(1100)
            _isLoading.value = false
        }
    }
}