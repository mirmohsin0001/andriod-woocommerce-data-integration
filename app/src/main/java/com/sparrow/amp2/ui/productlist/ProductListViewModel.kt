package com.sparrow.amp2.ui.productlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sparrow.amp2.domain.model.Product
import com.sparrow.amp2.domain.repository.ProductRepository
import com.sparrow.amp2.utils.DependencyContainer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class ProductListViewModel(
    private val productRepository: ProductRepository = DependencyContainer.productRepository
) : ViewModel() {
    
    private val _viewState = MutableStateFlow(ProductListViewState())
    val viewState = _viewState.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    
    val products: Flow<PagingData<Product>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) {
                productRepository.getProducts()
            } else {
                productRepository.searchProducts(query)
            }
        }
        .cachedIn(viewModelScope)
    
    fun toggleViewMode() {
        _viewState.value = _viewState.value.copy(
            isGridMode = !_viewState.value.isGridMode
        )
    }
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun updateSearchBarExpanded(expanded: Boolean) {
        _viewState.value = _viewState.value.copy(
            isSearchBarExpanded = expanded
        )
    }
}

data class ProductListViewState(
    val isGridMode: Boolean = true,
    val isSearchBarExpanded: Boolean = false
)
