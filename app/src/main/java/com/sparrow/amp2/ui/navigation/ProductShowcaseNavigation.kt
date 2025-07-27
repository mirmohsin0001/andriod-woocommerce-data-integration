package com.sparrow.amp2.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.sparrow.amp2.ui.contact.ContactScreen
import com.sparrow.amp2.ui.productdetail.ProductDetailScreen
import com.sparrow.amp2.ui.productlist.ProductListScreen

@Composable
fun ProductShowcaseNavigation(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    // Check if we should show bottom navigation
    val showBottomBar = currentDestination?.route in listOf(
        BottomNavItem.Home.route,
        BottomNavItem.Contact.route
    )
    
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) {
                ProductListScreen(
                    onProductClick = { productId ->
                        navController.navigate("${NavigationItem.ProductDetail.route}/$productId")
                    }
                )
            }
            
            composable(BottomNavItem.Contact.route) {
                ContactScreen()
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
                    },
                    onContactClick = {
                        navController.navigate(BottomNavItem.Contact.route)
                    }
                )
            }
        }
    }
}

sealed class NavigationItem(val route: String) {
    object ProductDetail : NavigationItem("product_detail")
}

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem("home", Icons.Default.Home, "Home")
    object Contact : BottomNavItem("contact", Icons.Default.Phone, "Contact")
}

val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Contact
)
