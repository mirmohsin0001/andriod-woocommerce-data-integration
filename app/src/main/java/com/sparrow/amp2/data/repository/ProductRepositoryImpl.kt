package com.sparrow.amp2.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sparrow.amp2.data.api.WooCommerceApiService
import com.sparrow.amp2.data.mapper.ProductMapper
import com.sparrow.amp2.data.paging.ProductPagingSource
import com.sparrow.amp2.domain.model.Product
import com.sparrow.amp2.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
class ProductRepositoryImpl(
    private val apiService: WooCommerceApiService,
    private val productMapper: ProductMapper
) : ProductRepository {
    
    companion object {
        const val NETWORK_PAGE_SIZE = 20
        const val CONSUMER_KEY = "ck_6e9b176a837bb9508842ea831370fc5a01b753e2"  // Replace with actual key
        const val CONSUMER_SECRET = "cs_9770dda61f8f7c1f1c8b4abd2bd6bc79fada9c71"  // Replace with actual secret
    }
    
    override fun getProducts(): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false,
                prefetchDistance = 5,
                initialLoadSize = NETWORK_PAGE_SIZE * 2
            ),
            pagingSourceFactory = {
                ProductPagingSource(
                    apiService = apiService,
                    consumerKey = CONSUMER_KEY,
                    consumerSecret = CONSUMER_SECRET,
                    productMapper = productMapper
                )
            }
        ).flow
    }
    
    override suspend fun getProduct(productId: Int): Result<Product> {
        return try {
            val response = apiService.getProduct(
                productId = productId,
                consumerKey = CONSUMER_KEY,
                consumerSecret = CONSUMER_SECRET
            )
            
            if (response.isSuccessful && response.body() != null) {
                val product = productMapper.mapToDomain(response.body()!!)
                Result.success(product)
            } else {
                Result.failure(Exception("Failed to fetch product: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun searchProducts(query: String): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ProductPagingSource(
                    apiService = apiService,
                    consumerKey = CONSUMER_KEY,
                    consumerSecret = CONSUMER_SECRET,
                    productMapper = productMapper,
                    searchQuery = query
                )
            }
        ).flow
    }
}
