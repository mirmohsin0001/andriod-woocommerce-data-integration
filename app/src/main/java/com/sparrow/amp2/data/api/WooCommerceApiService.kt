package com.sparrow.amp2.data.api

import com.sparrow.amp2.data.model.ProductResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WooCommerceApiService {
    
    @GET("products")
    suspend fun getProducts(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20,
        @Query("status") status: String = "publish",
        @Query("consumer_key") consumerKey: String,
        @Query("consumer_secret") consumerSecret: String
    ): Response<List<ProductResponse>>
    
    @GET("products/{id}")
    suspend fun getProduct(
        @Path("id") productId: Int,
        @Query("consumer_key") consumerKey: String,
        @Query("consumer_secret") consumerSecret: String
    ): Response<ProductResponse>
    
    @GET("products")
    suspend fun searchProducts(
        @Query("search") searchQuery: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20,
        @Query("consumer_key") consumerKey: String,
        @Query("consumer_secret") consumerSecret: String
    ): Response<List<ProductResponse>>
    
    @GET("products")
    suspend fun getProductsByCategory(
        @Query("category") categoryId: Int,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20,
        @Query("consumer_key") consumerKey: String,
        @Query("consumer_secret") consumerSecret: String
    ): Response<List<ProductResponse>>
}
