package com.sparrow.amp2.domain.repository

import androidx.paging.PagingData
import com.sparrow.amp2.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(): Flow<PagingData<Product>>
    suspend fun getProduct(productId: Int): Result<Product>
    fun searchProducts(query: String): Flow<PagingData<Product>>
}
