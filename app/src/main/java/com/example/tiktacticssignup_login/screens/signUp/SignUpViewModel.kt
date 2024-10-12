package com.example.tiktacticssignup_login.screens.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiktacticssignup_login.data.remote.TiktacticsApiService
import com.example.tiktacticssignup_login.data.remote.dtos.request.RegistrationDto
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {
    private val apiService = TiktacticsApiService.getInstance()

    private val _oneTimeEvents = MutableSharedFlow<OneTimeEvents>()
    val oneTimeEvents = _oneTimeEvents.asSharedFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun signUpUser(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ) {
        _isLoading.update { true }
        viewModelScope.launch {
            try {
                val registerDto = RegistrationDto(
                    firstName = firstName.trim(),
                    lastName = lastName.trim(),
                    email = email.trim(),
                    password1 = password.trim(),
                    password2 = password.trim(),
                    username = firstName
                )
                apiService.registerUser(registerDto)
                _isLoading.update { false }
                _oneTimeEvents.emit(OneTimeEvents.Navigate)
            } catch (e: Exception) {
                _oneTimeEvents.emit(OneTimeEvents.ShowToast(e.message ?: "Unknown Error"))
                _isLoading.update { false }
                e.printStackTrace()
            }
        }
    }
}


sealed interface OneTimeEvents {
    data object Navigate : OneTimeEvents
    data class ShowToast(val message: String) : OneTimeEvents
}

