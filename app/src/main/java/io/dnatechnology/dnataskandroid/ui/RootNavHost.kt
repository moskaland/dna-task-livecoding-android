package io.dnatechnology.dnataskandroid.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.devmoskal.feature.payment.navigateToPayment
import com.devmoskal.feature.payment.paymentScreen
import com.devmoskal.feature.products.popToProducts
import com.devmoskal.feature.products.productsRoute
import com.devmoskal.feature.products.productsScreen
import com.devmoskal.feature.purchase.checkout.checkoutScreen
import com.devmoskal.feature.purchase.checkout.navigateToCheckout
import com.devmoskal.feature.purchase.summary.navigateToSummary
import com.devmoskal.feature.purchase.summary.summaryScreen

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
        productsScreen(navController::navigateToCheckout)
        checkoutScreen(navController::navigateUp, navController::navigateToPayment)
        paymentScreen(navController::navigateUp, navController::navigateToSummary)
        summaryScreen(navController::popToProducts)
    }
}