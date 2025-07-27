package com.sparrow.amp2.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparrow.amp2.data.auth.AuthRepository
import com.sparrow.amp2.data.auth.AuthRepositoryImpl
import com.sparrow.amp2.utils.DependencyContainer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OtpVerificationViewModel(
    private val phoneNumber: String,
    private val authRepository: AuthRepository = DependencyContainer.authRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(OtpVerificationUiState())
    val uiState: StateFlow<OtpVerificationUiState> = _uiState.asStateFlow()
    
    init {
        startResendTimer()
    }
    
    fun verifyOtp(otpCode: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        
        viewModelScope.launch {
            try {
                // Get the stored verification ID from AuthRepository
                val verificationId = (authRepository as? AuthRepositoryImpl)?.getStoredVerificationId()
                
                if (verificationId != null) {
                    authRepository.verifyPhoneNumberWithCode(verificationId, otpCode)
                        .onSuccess { user ->
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                isVerified = true,
                                error = null
                            )
                        }
                        .onFailure { exception ->
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                error = exception.message ?: "Invalid OTP. Please try again."
                            )
                        }
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Verification ID not found. Please try again."
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Verification failed"
                )
            }
        }
    }
    
    fun resendOtp() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        
        viewModelScope.launch {
            try {
                // TODO: Implement resend OTP with Firebase
                delay(1000)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    canResend = false,
                    resendTimer = 60
                )
                startResendTimer()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to resend OTP"
                )
            }
        }
    }
    
    private fun startResendTimer() {
        viewModelScope.launch {
            repeat(60) { second ->
                _uiState.value = _uiState.value.copy(
                    resendTimer = 60 - second,
                    canResend = false
                )
                delay(1000)
            }
            _uiState.value = _uiState.value.copy(
                canResend = true,
                resendTimer = 0
            )
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class OtpVerificationUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isVerified: Boolean = false,
    val canResend: Boolean = false,
    val resendTimer: Int = 60
)
