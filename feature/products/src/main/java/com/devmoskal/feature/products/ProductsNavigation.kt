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
        ProductsScreen(productsViewModel)
    }
}
