package com.sparrow.amp2.utils

import com.sparrow.amp2.data.api.WooCommerceApiService
import com.sparrow.amp2.data.mapper.ProductMapper
import com.sparrow.amp2.data.repository.ProductRepositoryImpl
import com.sparrow.amp2.domain.repository.ProductRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object DependencyContainer {
    
    private const val BASE_URL = "https://backend.gardenblossom.store/wp-json/wc/v3/"
    
    private val httpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
    
    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    val wooCommerceApiService: WooCommerceApiService by lazy {
        retrofit.create(WooCommerceApiService::class.java)
    }
    
    val productMapper: ProductMapper by lazy {
        ProductMapper()
    }
    
    val productRepository: ProductRepository by lazy {
        ProductRepositoryImpl(wooCommerceApiService, productMapper)
    }
}
