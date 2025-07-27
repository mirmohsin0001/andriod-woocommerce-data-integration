package com.sparrow.amp2.ui.productlist

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.sparrow.amp2.R
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.sparrow.amp2.domain.model.Product
import com.sparrow.amp2.ui.components.ErrorMessage
import com.sparrow.amp2.ui.components.LoadingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    onProductClick: (Int) -> Unit,
    categoryId: Int? = null,
    categoryName: String? = null,
    onBackClick: (() -> Unit)? = null,
    viewModel: ProductListViewModel = viewModel {
        ProductListViewModel(categoryId = categoryId)
    }
) {
    val viewState by viewModel.viewState.collectAsState()
    val products = viewModel.products.collectAsLazyPagingItems()
    var searchQuery by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        // Top App Bar with Search and View Toggle
        TopAppBar(
            navigationIcon = {
                if (categoryName != null && onBackClick != null) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            },
            title = {
                if (viewState.isSearchBarExpanded) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { 
                            searchQuery = it
                            viewModel.updateSearchQuery(it)
                        },
                        placeholder = { Text("Search products...") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics { contentDescription = "Search products text field" }
                    )
                } else if (categoryName != null) {
                    Text(
                        text = categoryName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Custom Logo
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "Garden Blossom Logo",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Fit
                        )
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Column {
                            Text(
                                text = "Kashmir Aromatics",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Valley's Aromatic Tale",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            },
            actions = {
                IconButton(
                    onClick = { 
                        viewModel.updateSearchBarExpanded(!viewState.isSearchBarExpanded)
                        if (!viewState.isSearchBarExpanded) {
                            searchQuery = ""
                            viewModel.updateSearchQuery("")
                        }
                    }
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = if (viewState.isSearchBarExpanded) "Close search" else "Open search"
                    )
                }
                
                TextButton(
                    onClick = { viewModel.toggleViewMode() }
                ) {
                    Text(if (viewState.isGridMode) "List" else "Grid")
                }
            },
            modifier = Modifier.animateContentSize()
        )
        
        // Product List/Grid
        when (products.loadState.refresh) {
            is LoadState.Loading -> {
                LoadingIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            }
            is LoadState.Error -> {
                ErrorMessage(
                    message = "Failed to load products. Please try again.",
                    onRetry = { products.retry() },
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            }
            else -> {
                if (viewState.isGridMode) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            count = products.itemCount,
                            key = products.itemKey { it.id },
                            span = { GridItemSpan(1) }
                        ) { index ->
                            val product = products[index]
                            product?.let {
                                ProductGridItem(
                                    product = it,
                                    onClick = { onProductClick(it.id) }
                                )
                            }
                        }
                        
                        item(span = { GridItemSpan(2) }) {
                            when (products.loadState.append) {
                                is LoadState.Loading -> {
                                    LoadingIndicator(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                            .wrapContentWidth(Alignment.CenterHorizontally)
                                    )
                                }
                                is LoadState.Error -> {
                                    ErrorMessage(
                                        message = "Failed to load more products",
                                        onRetry = { products.retry() },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                    )
                                }
                                else -> {}
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            count = products.itemCount,
                            key = products.itemKey { it.id }
                        ) { index ->
                            val product = products[index]
                            product?.let {
                                ProductListItem(
                                    product = it,
                                    onClick = { onProductClick(it.id) }
                                )
                            }
                        }
                        
                        item {
                            when (products.loadState.append) {
                                is LoadState.Loading -> {
                                    LoadingIndicator(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                            .wrapContentWidth(Alignment.CenterHorizontally)
                                    )
                                }
                                is LoadState.Error -> {
                                    ErrorMessage(
                                        message = "Failed to load more products",
                                        onRetry = { products.retry() },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                    )
                                }
                                else -> {}
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductGridItem(
    product: Product,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .semantics { contentDescription = "Product ${product.name}" },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            // Product Image
            GlideImage(
                model = product.images.firstOrNull()?.src ?: "",
                contentDescription = product.images.firstOrNull()?.alt ?: product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            )
            
            // Product Details
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Category
                if (product.categories.isNotEmpty()) {
                    val categoryName = product.categories.first().name
                    val (backgroundColor, textColor) = when (categoryName.lowercase()) {
                        "flower" -> Pair(Color(0xFF4CAF50).copy(alpha = 0.15f), Color(0xFF1B5E20))
                        "fruit" -> Pair(Color(0xFFFF9800).copy(alpha = 0.15f), Color(0xFFE65100))
                        else -> Pair(Color(0xFF2196F3).copy(alpha = 0.15f), Color(0xFF0D47A1))
                    }
                    
                    Text(
                        text = categoryName,
                        style = MaterialTheme.typography.bodySmall,
                        color = textColor,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .background(
                                backgroundColor,
                                RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                
                Text(
                    text = "₹${product.price}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                if (product.onSale && !product.salePrice.isNullOrEmpty()) {
                    Text(
                        text = "₹${product.regularPrice}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                        textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductListItem(
    product: Product,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .semantics { contentDescription = "Product ${product.name}" },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp)
        ) {
            // Product Image
            GlideImage(
                model = product.images.firstOrNull()?.src ?: "",
                contentDescription = product.images.firstOrNull()?.alt ?: product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Product Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = product.shortDescription.take(100) + if (product.shortDescription.length > 100) "..." else "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Category
                if (product.categories.isNotEmpty()) {
                    val categoryName = product.categories.first().name
                    val (backgroundColor, textColor) = when (categoryName.lowercase()) {
                        "flower" -> Pair(Color(0xFF4CAF50).copy(alpha = 0.15f), Color(0xFF1B5E20))
                        "fruit" -> Pair(Color(0xFFFF9800).copy(alpha = 0.15f), Color(0xFFE65100))
                        else -> Pair(Color(0xFF2196F3).copy(alpha = 0.15f), Color(0xFF0D47A1))
                    }
                    
                    Text(
                        text = categoryName,
                        style = MaterialTheme.typography.bodySmall,
                        color = textColor,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .background(
                                backgroundColor,
                                RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "₹${product.price}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    if (product.onSale && !product.salePrice.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "₹${product.regularPrice}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.outline,
                            textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                        )
                    }
                }
            }
        }
    }
}
