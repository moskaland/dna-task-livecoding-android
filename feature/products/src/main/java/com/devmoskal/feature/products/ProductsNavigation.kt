package com.devmoskal.feature.products

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val productsRoute = "products_route"

fun NavGraphBuilder.productsScreen() {
    composable(route = productsRoute) {
        ProductsRoute()
    }
}

@Composable
fun ProductsRoute(productsViewModel: ProductsViewModel = hiltViewModel()) {
    val products by productsViewModel.products.collectAsStateWithLifecycle()
    val cart by productsViewModel.cart.collectAsStateWithLifecycle()

    ProductsScreen(
        products,
        cart,
        productsViewModel::getProducts,
        productsViewModel::addToCart,
        productsViewModel::removeFromCart
    )
}


