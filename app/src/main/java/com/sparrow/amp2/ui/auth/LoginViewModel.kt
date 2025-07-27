package com.sparrow.amp2.ui.auth

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparrow.amp2.data.auth.AuthRepository
import com.sparrow.amp2.utils.DependencyContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository = DependencyContainer.authRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()
    
    fun signInWithGoogle(idToken: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        
        viewModelScope.launch {
            authRepository.signInWithGoogle(idToken)
                .onSuccess { user ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoginSuccessful = true,
                        error = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Google Sign In failed"
                    )
                }
        }
    }
    
    fun signInWithFacebook() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        
        viewModelScope.launch {
            try {
                // TODO: Implement Facebook Sign In with Firebase
                // For now, show not implemented message
                kotlinx.coroutines.delay(1000)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Facebook Sign In coming soon!"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Sign in failed"
                )
            }
        }
    }

    fun sendPhoneOtp(phoneNumber: String, activity: Activity) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        // Validate phone number format
        val formattedPhoneNumber = if (!phoneNumber.startsWith("+")) {
            if (phoneNumber.startsWith("91")) {
                "+$phoneNumber"
            } else if (phoneNumber.startsWith("0")) {
                "+91${phoneNumber.substring(1)}"
            } else {
                "+91$phoneNumber"
            }
        } else {
            phoneNumber
        }

        viewModelScope.launch {
            authRepository.signInWithPhoneNumber(formattedPhoneNumber, activity)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isPhoneOtpSent = true
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to send OTP"
                    )
                }
        }
    }



    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    fun updateError(message: String) {
        _uiState.value = _uiState.value.copy(error = message, isLoading = false)
    }
    
    fun resetState() {
        _uiState.value = LoginUiState()
    }
}

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isPhoneOtpSent: Boolean = false,
    val isLoginSuccessful: Boolean = false
)
