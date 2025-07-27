package com.sparrow.amp2.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparrow.amp2.data.auth.AuthRepository
import com.sparrow.amp2.utils.DependencyContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authRepository: AuthRepository = DependencyContainer.authRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    
    init {
        observeAuthState()
    }
    
    private fun observeAuthState() {
        viewModelScope.launch {
            authRepository.currentUser.collectLatest { user ->
                _uiState.value = _uiState.value.copy(
                    isLoggedIn = user != null,
                    user = user,
                    isLoading = false
                )
            }
        }
    }
    
    fun logout() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                
                authRepository.signOut()
                    .onSuccess {
                        _uiState.value = ProfileUiState(
                            isLoggedIn = false,
                            user = null,
                            isLoading = false
                        )
                    }
                    .onFailure { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = exception.message ?: "Logout failed"
                        )
                    }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Logout failed"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class ProfileUiState(
    val isLoggedIn: Boolean = false,
    val user: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

data class User(
    val id: String,
    val name: String,
    val email: String? = null,
    val phoneNumber: String? = null,
    val photoUrl: String? = null,
    val isEmailVerified: Boolean = false,
    val isPhoneVerified: Boolean = false
)
