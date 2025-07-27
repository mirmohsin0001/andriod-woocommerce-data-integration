package com.sparrow.amp2.domain.repository

import androidx.paging.PagingData
import com.sparrow.amp2.domain.model.Product
import com.sparrow.amp2.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(): Flow<PagingData<Product>>
    suspend fun getProduct(productId: Int): Result<Product>
    fun searchProducts(query: String): Flow<PagingData<Product>>
    suspend fun getCategories(): Result<List<Category>>
    fun getProductsByCategory(categoryId: Int): Flow<PagingData<Product>>
}
