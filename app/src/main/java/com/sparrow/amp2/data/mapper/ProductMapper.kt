package com.sparrow.amp2.data.mapper

import com.sparrow.amp2.data.model.ProductResponse
import com.sparrow.amp2.domain.model.Category
import com.sparrow.amp2.domain.model.Dimensions
import com.sparrow.amp2.domain.model.Product
import com.sparrow.amp2.domain.model.ProductAttribute
import com.sparrow.amp2.domain.model.ProductImage

class ProductMapper {
    
    fun mapToDomain(productResponse: ProductResponse): Product {
        return Product(
            id = productResponse.id,
            name = productResponse.name,
            description = productResponse.description,
            shortDescription = productResponse.shortDescription,
            price = productResponse.price,
            regularPrice = productResponse.regularPrice,
            salePrice = productResponse.salePrice,
            onSale = productResponse.onSale,
            stockStatus = productResponse.stockStatus,
            averageRating = productResponse.averageRating,
            ratingCount = productResponse.ratingCount,
            categories = productResponse.categories.map { categoryResponse ->
                Category(
                    id = categoryResponse.id,
                    name = categoryResponse.name,
                    slug = categoryResponse.slug
                )
            },
            images = productResponse.images.map { imageResponse ->
                ProductImage(
                    id = imageResponse.id,
                    src = imageResponse.src,
                    name = imageResponse.name,
                    alt = imageResponse.alt
                )
            },
            attributes = productResponse.attributes.map { attributeResponse ->
                ProductAttribute(
                    id = attributeResponse.id,
                    name = attributeResponse.name,
                    options = attributeResponse.options,
                    visible = attributeResponse.visible
                )
            },
            sku = productResponse.sku,
            weight = productResponse.weight,
            dimensions = Dimensions(
                length = productResponse.dimensions.length,
                width = productResponse.dimensions.width,
                height = productResponse.dimensions.height
            ),
            featured = productResponse.featured,
            permalink = productResponse.permalink
        )
    }
}
