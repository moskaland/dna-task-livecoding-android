package io.dnatechnology.dnataskandroid.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.devmoskal.feature.products.ProductsViewModel
import com.devmoskal.feature.products.productsRoute
import com.devmoskal.feature.products.productsScreen

@Composable
fun RootNavHost(
    navController: NavHostController,
    productsViewModel: ProductsViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = productsRoute,
        modifier = modifier
    ) {
        productsScreen(productsViewModel)
    }
}