package com.sparrow.amp2.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sparrow.amp2.ui.productdetail.ProductDetailScreen
import com.sparrow.amp2.ui.productlist.ProductListScreen

@Composable
fun ProductShowcaseNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavigationItem.ProductList.route
    ) {
        composable(NavigationItem.ProductList.route) {
            ProductListScreen(
                onProductClick = { productId ->
                    navController.navigate("${NavigationItem.ProductDetail.route}/$productId")
                }
            )
        }
        
        composable(
            route = "${NavigationItem.ProductDetail.route}/{productId}",
            arguments = listOf(
                navArgument("productId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: 0
            ProductDetailScreen(
                productId = productId,
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
    }
}

sealed class NavigationItem(val route: String) {
    object ProductList : NavigationItem("product_list")
    object ProductDetail : NavigationItem("product_detail")
}
