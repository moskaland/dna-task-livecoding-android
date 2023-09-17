package com.devmoskal.feature.payment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val paymentRoute = "payment_route"

fun NavController.navigateToPayment() {
    navigate(paymentRoute) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.paymentScreen(navigateUp: () -> Boolean, navigateOnPaymentSucceed: () -> Unit) {
    composable(route = paymentRoute) {
        PaymentRoute(navigateUp, navigateOnPaymentSucceed)
    }
}

@Composable
fun PaymentRoute(
    navigateUp: () -> Boolean,
    navigateOnPaymentSucceed: () -> Unit,
    paymentViewModel: PaymentViewModel = hiltViewModel()
) {
    val uiState by paymentViewModel.paymentUiState.collectAsStateWithLifecycle()
    PaymentScreen(
        uiState,
        paymentViewModel::pay,
        paymentViewModel::onErrorAcknowledgeByUser,
        navigateUp,
        navigateOnPaymentSucceed
    )
}


