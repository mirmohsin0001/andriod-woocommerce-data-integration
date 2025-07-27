package com.sparrow.amp2.domain.model

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val shortDescription: String,
    val price: String,
    val regularPrice: String,
    val salePrice: String?,
    val onSale: Boolean,
    val stockStatus: String,
    val averageRating: String,
    val ratingCount: Int,
    val categories: List<Category>,
    val images: List<ProductImage>,
    val attributes: List<ProductAttribute>,
    val sku: String,
    val weight: String,
    val dimensions: Dimensions,
    val featured: Boolean,
    val permalink: String
)

data class Category(
    val id: Int,
    val name: String,
    val slug: String
)

data class ProductImage(
    val id: Int,
    val src: String,
    val name: String,
    val alt: String
)

data class ProductAttribute(
    val id: Int,
    val name: String,
    val options: List<String>,
    val visible: Boolean
)

data class Dimensions(
    val length: String,
    val width: String,
    val height: String
)
