package com.sparrow.amp2.ui.productdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sparrow.amp2.domain.model.Product
import com.sparrow.amp2.domain.repository.ProductRepository
import com.sparrow.amp2.utils.DependencyContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val productRepository: ProductRepository = DependencyContainer.productRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState = _uiState.asStateFlow()
    
    fun loadProduct(productId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            productRepository.getProduct(productId)
                .onSuccess { product ->
                    _uiState.value = _uiState.value.copy(
                        product = product,
                        isLoading = false,
                        error = null
                    )
                }
                .onFailure { throwable ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = throwable.message ?: "Unknown error occurred"
                    )
                }
        }
    }
    
    fun selectTab(tab: ProductDetailTab) {
        _uiState.value = _uiState.value.copy(selectedTab = tab)
    }
    
    fun updateCurrentImageIndex(index: Int) {
        _uiState.value = _uiState.value.copy(currentImageIndex = index)
    }
    
    fun addToCart() {
        // TODO: Implement add to cart functionality
        _uiState.value = _uiState.value.copy(
            showAddToCartMessage = true
        )
    }
    
    fun dismissAddToCartMessage() {
        _uiState.value = _uiState.value.copy(
            showAddToCartMessage = false
        )
    }
}

data class ProductDetailUiState(
    val product: Product? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedTab: ProductDetailTab = ProductDetailTab.DESCRIPTION,
    val currentImageIndex: Int = 0,
    val showAddToCartMessage: Boolean = false
)

enum class ProductDetailTab(val title: String) {
    DESCRIPTION("Description"),
    SPECIFICATIONS("Specifications"),
    REVIEWS("Reviews")
}
