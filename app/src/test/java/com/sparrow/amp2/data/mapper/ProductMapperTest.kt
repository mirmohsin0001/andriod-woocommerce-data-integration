package com.sparrow.amp2.data.mapper

import com.sparrow.amp2.data.model.*
import org.junit.Test
import org.junit.Assert.*

class ProductMapperTest {

    private val productMapper = ProductMapper()

    @Test
    fun mapToDomain_shouldMapAllFields() {
        // Given
        val productResponse = ProductResponse(
            id = 1,
            name = "Test Product",
            slug = "test-product",
            permalink = "https://example.com/test-product",
            dateCreated = "2024-01-01T00:00:00",
            dateModified = "2024-01-01T00:00:00",
            type = "simple",
            status = "publish",
            featured = true,
            catalogVisibility = "visible",
            description = "Test description",
            shortDescription = "Short description",
            sku = "TEST-001",
            price = "99.99",
            regularPrice = "99.99",
            salePrice = null,
            dateOnSaleFrom = null,
            dateOnSaleTo = null,
            priceHtml = "$99.99",
            onSale = false,
            purchasable = true,
            totalSales = 10,
            virtual = false,
            downloadable = false,
            externalUrl = "",
            buttonText = "",
            taxStatus = "taxable",
            taxClass = "",
            manageStock = false,
            stockQuantity = null,
            stockStatus = "instock",
            backorders = "no",
            backordersAllowed = false,
            backordered = false,
            soldIndividually = false,
            weight = "1.5",
            dimensions = DimensionsResponse("10", "5", "3"),
            shippingRequired = true,
            shippingTaxable = true,
            shippingClass = "",
            shippingClassId = 0,
            reviewsAllowed = true,
            averageRating = "4.5",
            ratingCount = 10,
            relatedIds = listOf(2, 3),
            upsellIds = listOf(),
            crossSellIds = listOf(),
            parentId = 0,
            purchaseNote = "",
            categories = listOf(
                CategoryResponse(1, "Electronics", "electronics")
            ),
            tags = listOf(),
            images = listOf(
                ImageResponse(1, "2024-01-01T00:00:00", "2024-01-01T00:00:00", "https://example.com/image.jpg", "Test Image", "Test Alt")
            ),
            attributes = listOf(
                AttributeResponse(1, "Color", 0, true, false, listOf("Red", "Blue"))
            ),
            defaultAttributes = listOf(),
            variations = listOf(),
            groupedProducts = listOf(),
            menuOrder = 0,
            metaData = listOf()
        )

        // When
        val product = productMapper.mapToDomain(productResponse)

        // Then
        assertEquals(1, product.id)
        assertEquals("Test Product", product.name)
        assertEquals("Test description", product.description)
        assertEquals("Short description", product.shortDescription)
        assertEquals("99.99", product.price)
        assertEquals("99.99", product.regularPrice)
        assertNull(product.salePrice)
        assertFalse(product.onSale)
        assertEquals("instock", product.stockStatus)
        assertEquals("4.5", product.averageRating)
        assertEquals(10, product.ratingCount)
        assertEquals(1, product.categories.size)
        assertEquals("Electronics", product.categories[0].name)
        assertEquals(1, product.images.size)
        assertEquals("https://example.com/image.jpg", product.images[0].src)
        assertEquals(1, product.attributes.size)
        assertEquals("Color", product.attributes[0].name)
        assertEquals("TEST-001", product.sku)
        assertEquals("1.5", product.weight)
        assertEquals("10", product.dimensions.length)
        assertTrue(product.featured)
        assertEquals("https://example.com/test-product", product.permalink)
    }
}
