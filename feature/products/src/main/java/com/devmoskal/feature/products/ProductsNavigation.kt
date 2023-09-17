package com.devmoskal.feature.products

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val productsRoute = "products_route"

fun NavController.popToProducts() {
    navigate(productsRoute) {
        popUpTo(productsRoute)
    }
}

fun NavGraphBuilder.productsScreen(onPayClick: () -> Unit) {
    composable(route = productsRoute) {
        ProductsRoute(onPayClick)
    }
}

@Composable
fun ProductsRoute(onPayClick: () -> Unit, productsViewModel: ProductsViewModel = hiltViewModel()) {
    val products by productsViewModel.products.collectAsStateWithLifecycle()
    val cart by productsViewModel.cart.collectAsStateWithLifecycle()

    ProductsScreen(
        products,
        cart,
        productsViewModel::getProducts,
        productsViewModel::addToCart,
        productsViewModel::removeFromCart,
        onPayClick,
    )
}


