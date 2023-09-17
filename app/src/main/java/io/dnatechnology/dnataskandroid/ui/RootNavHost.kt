package io.dnatechnology.dnataskandroid.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.devmoskal.feature.payment.navigateToPayment
import com.devmoskal.feature.payment.paymentScreen
import com.devmoskal.feature.products.productsRoute
import com.devmoskal.feature.products.productsScreen
import com.devmoskal.feature.purchase.navigateToPurchase
import com.devmoskal.feature.purchase.purchaseScreen

@Composable
fun RootNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = productsRoute,
        modifier = modifier
    ) {
        productsScreen(navController::navigateToPurchase)
        purchaseScreen(navController::navigateUp, navController::navigateToPayment)
        paymentScreen(navController::navigateUp)
    }
}