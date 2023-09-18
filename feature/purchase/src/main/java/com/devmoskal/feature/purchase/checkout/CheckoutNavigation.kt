package com.devmoskal.feature.purchase.checkout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val checkoutRoute = "checkout_route"

fun NavController.navigateToCheckout() {
    navigate(checkoutRoute) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.CheckoutScreen(navigateUp: () -> Boolean, navigateToPayment: () -> Unit) {
    composable(route = checkoutRoute) {
        CheckoutRoute(navigateUp, navigateToPayment)
    }
}

@Composable
fun CheckoutRoute(
    navigateUp: () -> Boolean,
    navigateToPayment: () -> Unit,
    checkoutViewModel: CheckoutViewModel = hiltViewModel()
) {
    val uiState by checkoutViewModel.checkoutUiState.collectAsStateWithLifecycle()
    CheckoutScreen(
        uiState,
        checkoutViewModel::cleanup,
        navigateToPayment,
        navigateUp
    )
}


