package com.devmoskal.feature.products

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val productsRoute = "products_route"

fun NavGraphBuilder.productsScreen(
    productsViewModel: ProductsViewModel
) {
    composable(route = productsRoute) {
        ProductsRoute(productsViewModel)
    }
}

@Composable
fun ProductsRoute(productsViewModel: ProductsViewModel) {
    ProductsScreen(
        productsViewModel.products.collectAsState().value,
        productsViewModel.cart.collectAsState().value,
        productsViewModel::getProducts,
        productsViewModel::addToCart,
        productsViewModel::removeFromCart
    )
}


