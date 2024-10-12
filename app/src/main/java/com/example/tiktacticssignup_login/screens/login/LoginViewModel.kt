package com.example.tiktacticssignup_login.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiktacticssignup_login.data.remote.TiktacticsApiService
import com.example.tiktacticssignup_login.data.remote.dtos.request.LoginRequestDto
import com.example.tiktacticssignup_login.data.remote.dtos.response.LoginResponseDto
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val apiService = TiktacticsApiService.getInstance()


    private val _oneTimeEvents = MutableSharedFlow<OneTimeEvents>()
    val oneTimeEvents = _oneTimeEvents.asSharedFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun signInUser(
        email: String,
        password: String
    ) {
        _isLoading.update { true }
        viewModelScope.launch {
            try {
                val loginRequestDto = LoginRequestDto(
                    email = email.trim(),
                    password = password.trim()
                )
                val loginResponseDto = apiService.loginUser(loginRequestDto)
                _isLoading.update { false }
                _oneTimeEvents.emit(OneTimeEvents.Navigate(loginResponseDto))
            } catch (e: Exception) {
                e.printStackTrace()
                _isLoading.update { false }
                _oneTimeEvents.emit(OneTimeEvents.ShowToast(e.message ?: "Unknown Error"))
            }
        }
    }
}

sealed interface OneTimeEvents {
    data class Navigate(val loginResponseDto: LoginResponseDto) : OneTimeEvents
    data class ShowToast(val message: String) : OneTimeEvents
}