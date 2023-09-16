package com.devmoskal.feature.payment

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val paymentRoute = "payment_route"

fun NavController.navigateToPayment() {
    navigate(paymentRoute) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.paymentScreen() {
    composable(route = paymentRoute) {
        PaymentRoute()
    }
}

@Composable
fun PaymentRoute(paymentViewModel: PaymentViewModel = hiltViewModel()) {
    PaymentScreen(paymentViewModel.test)
}


