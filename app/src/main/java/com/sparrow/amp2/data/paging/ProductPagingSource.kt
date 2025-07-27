package com.sparrow.amp2.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sparrow.amp2.data.api.WooCommerceApiService
import com.sparrow.amp2.data.mapper.ProductMapper
import com.sparrow.amp2.domain.model.Product
import retrofit2.HttpException
import java.io.IOException

class ProductPagingSource(
    private val apiService: WooCommerceApiService,
    private val consumerKey: String,
    private val consumerSecret: String,
    private val productMapper: ProductMapper,
    private val searchQuery: String? = null,
    private val categoryId: Int? = null
) : PagingSource<Int, Product>() {
    
    companion object {
        const val STARTING_PAGE_INDEX = 1
    }
    
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val page = params.key ?: STARTING_PAGE_INDEX
        
        return try {
            val response = when {
                !searchQuery.isNullOrBlank() -> {
                    apiService.searchProducts(
                        searchQuery = searchQuery,
                        page = page,
                        perPage = params.loadSize,
                        consumerKey = consumerKey,
                        consumerSecret = consumerSecret
                    )
                }
                categoryId != null -> {
                    apiService.getProductsByCategory(
                        categoryId = categoryId,
                        page = page,
                        perPage = params.loadSize,
                        consumerKey = consumerKey,
                        consumerSecret = consumerSecret
                    )
                }
                else -> {
                    apiService.getProducts(
                        page = page,
                        perPage = params.loadSize,
                        consumerKey = consumerKey,
                        consumerSecret = consumerSecret
                    )
                }
            }
            
            if (response.isSuccessful) {
                val products = response.body()?.map { productResponse ->
                    productMapper.mapToDomain(productResponse)
                } ?: emptyList()
                
                LoadResult.Page(
                    data = products,
                    prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                    nextKey = if (products.isEmpty()) null else page + 1
                )
            } else {
                LoadResult.Error(HttpException(response))
            }
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
    
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
