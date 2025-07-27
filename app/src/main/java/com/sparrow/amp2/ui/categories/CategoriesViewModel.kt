package com.sparrow.amp2.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparrow.amp2.domain.model.Category
import com.sparrow.amp2.domain.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoriesViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<CategoriesUiState>(CategoriesUiState.Loading)
    val uiState: StateFlow<CategoriesUiState> = _uiState.asStateFlow()
    
    fun loadCategories() {
        _uiState.value = CategoriesUiState.Loading
        
        viewModelScope.launch {
            productRepository.getCategories()
                .onSuccess { categories ->
                    _uiState.value = CategoriesUiState.Success(categories)
                }
                .onFailure { exception ->
                    _uiState.value = CategoriesUiState.Error(
                        exception.message ?: "Unknown error occurred"
                    )
                }
        }
    }
}

sealed class CategoriesUiState {
    object Loading : CategoriesUiState()
    data class Success(val categories: List<Category>) : CategoriesUiState()
    data class Error(val message: String) : CategoriesUiState()
}
