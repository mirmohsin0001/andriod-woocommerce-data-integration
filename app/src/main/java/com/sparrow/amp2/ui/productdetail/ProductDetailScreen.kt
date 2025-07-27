package com.sparrow.amp2.ui.productdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.sparrow.amp2.domain.model.Product
import com.sparrow.amp2.ui.components.ErrorMessage
import com.sparrow.amp2.ui.components.LoadingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: Int,
    onBackClick: () -> Unit,
    viewModel: ProductDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Product Details") },
            navigationIcon = {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.semantics { contentDescription = "Back button" }
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )
        
        when {
            uiState.isLoading -> {
                LoadingIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            }
            uiState.error != null -> {
                ErrorMessage(
                    message = uiState.error!!,
                    onRetry = { viewModel.loadProduct(productId) },
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            }
            uiState.product != null -> {
                ProductDetailContent(
                    product = uiState.product!!,
                    selectedTab = uiState.selectedTab,
                    currentImageIndex = uiState.currentImageIndex,
                    onTabSelected = viewModel::selectTab,
                    onImageIndexChanged = viewModel::updateCurrentImageIndex,
                    onAddToCartClick = viewModel::addToCart
                )
            }
        }
        
        // Show Add to Cart Success Message
        if (uiState.showAddToCartMessage) {
            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(2000)
                viewModel.dismissAddToCartMessage()
            }
        }
    }
    
    // Snackbar for Add to Cart
    if (uiState.showAddToCartMessage) {
        LaunchedEffect(Unit) {
            // Show snackbar (implement with SnackbarHost if needed)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductDetailContent(
    product: Product,
    selectedTab: ProductDetailTab,
    currentImageIndex: Int,
    onTabSelected: (ProductDetailTab) -> Unit,
    onImageIndexChanged: (Int) -> Unit,
    onAddToCartClick: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { product.images.size })
    
    LaunchedEffect(pagerState.currentPage) {
        onImageIndexChanged(pagerState.currentPage)
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp) // Space for FAB
        ) {
            item {
                // Image Carousel
                if (product.images.isNotEmpty()) {
                    Column {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        ) { page ->
                            GlideImage(
                                model = product.images[page].src,
                                contentDescription = product.images[page].alt.ifEmpty { product.name },
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .semantics { 
                                        contentDescription = "Product image ${page + 1} of ${product.images.size}"
                                    }
                            )
                        }
                        
                        // Page Indicators
                        if (product.images.size > 1) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                repeat(product.images.size) { index ->
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (index == currentImageIndex) {
                                                    MaterialTheme.colorScheme.primary
                                                } else {
                                                    MaterialTheme.colorScheme.outline
                                                }
                                            )
                                    )
                                    if (index < product.images.size - 1) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            item {
                // Product Info
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "$${product.price}",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            
                            if (product.onSale && !product.salePrice.isNullOrEmpty()) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "$${product.regularPrice}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.outline,
                                    textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                                )
                            }
                        }
                        
                        if (product.averageRating.isNotEmpty() && product.averageRating != "0") {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFFA000),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${product.averageRating} (${product.ratingCount} reviews)",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Stock Status
                        Text(
                            text = when (product.stockStatus) {
                                "instock" -> "In Stock"
                                "outofstock" -> "Out of Stock"
                                "onbackorder" -> "On Backorder"
                                else -> product.stockStatus
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = when (product.stockStatus) {
                                "instock" -> Color.Green
                                "outofstock" -> Color.Red
                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }
                }
            }
            
            item {
                // Tab Section
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column {
                        // Tab Row
                        TabRow(
                            selectedTabIndex = selectedTab.ordinal,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ProductDetailTab.values().forEach { tab ->
                                Tab(
                                    selected = selectedTab == tab,
                                    onClick = { onTabSelected(tab) },
                                    text = { Text(tab.title) },
                                    modifier = Modifier.semantics { 
                                        contentDescription = "${tab.title} tab"
                                    }
                                )
                            }
                        }
                        
                        // Tab Content
                        when (selectedTab) {
                            ProductDetailTab.DESCRIPTION -> {
                                DescriptionTabContent(product)
                            }
                            ProductDetailTab.SPECIFICATIONS -> {
                                SpecificationsTabContent(product)
                            }
                            ProductDetailTab.REVIEWS -> {
                                ReviewsTabContent(product)
                            }
                        }
                    }
                }
            }
        }
        
        // Sticky Add to Cart Button
        ExtendedFloatingActionButton(
            onClick = onAddToCartClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .semantics { contentDescription = "Add to cart" },
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add to Cart")
        }
    }
}

@Composable
fun DescriptionTabContent(product: Product) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        if (product.shortDescription.isNotEmpty()) {
            Text(
                text = product.shortDescription,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
        
        if (product.description.isNotEmpty()) {
            val description = remember(product.description) {
                HtmlCompat.fromHtml(product.description, HtmlCompat.FROM_HTML_MODE_COMPACT).toString()
            }
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            Text(
                text = "No description available.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun SpecificationsTabContent(product: Product) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        if (product.attributes.isNotEmpty()) {
            product.attributes.filter { it.visible }.forEach { attribute ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = attribute.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = attribute.options.joinToString(", "),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End
                    )
                }
                Divider(modifier = Modifier.padding(vertical = 4.dp))
            }
        }
        
        // Additional specs
        if (product.weight.isNotEmpty()) {
            SpecRow("Weight", product.weight)
        }
        
        if (product.dimensions.length.isNotEmpty() || 
            product.dimensions.width.isNotEmpty() || 
            product.dimensions.height.isNotEmpty()) {
            val dimensionText = "${product.dimensions.length} × ${product.dimensions.width} × ${product.dimensions.height}"
            SpecRow("Dimensions", dimensionText)
        }
        
        if (product.sku.isNotEmpty()) {
            SpecRow("SKU", product.sku)
        }
        
        if (product.attributes.isEmpty() && product.weight.isEmpty() && 
            product.dimensions.length.isEmpty() && product.sku.isEmpty()) {
            Text(
                text = "No specifications available.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun SpecRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End
        )
    }
    Divider(modifier = Modifier.padding(vertical = 4.dp))
}

@Composable
fun ReviewsTabContent(product: Product) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        if (product.ratingCount > 0) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFA000),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${product.averageRating}/5.0",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "(${product.ratingCount} reviews)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Reviews functionality will be implemented in a future version.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Text(
                text = "No reviews yet.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
