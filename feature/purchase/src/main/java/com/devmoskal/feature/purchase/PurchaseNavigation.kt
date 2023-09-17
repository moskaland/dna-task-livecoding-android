package com.devmoskal.feature.purchase

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val purchaseRoute = "purchase_route"

fun NavController.navigateToPurchase() {
    navigate(purchaseRoute) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.purchaseScreen(navigateUp: () -> Boolean, navigateToPayment: () -> Unit) {
    composable(route = purchaseRoute) {
        PurchaseRoute(navigateUp, navigateToPayment)
    }
}

@Composable
fun PurchaseRoute(
    navigateUp: () -> Boolean,
    navigateToPayment: () -> Unit,
    purchaseViewModel: PurchaseViewModel = hiltViewModel()
) {
    val uiState by purchaseViewModel.purchaseUiState.collectAsStateWithLifecycle()
    PurchaseScreen(uiState, purchaseViewModel::cleanup, navigateToPayment, navigateUp)
}


