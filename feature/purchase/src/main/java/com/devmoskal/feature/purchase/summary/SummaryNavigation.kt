package com.devmoskal.feature.purchase.summary

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val summaryRoute = "summary_route"

fun NavController.navigateToSummary() {
    navigate(summaryRoute) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.summaryScreen(navigateOutOfPurchaseFlow: () -> Unit) {
    composable(route = summaryRoute) {
        SummaryRoute(navigateOutOfPurchaseFlow)
    }
}

@Composable
fun SummaryRoute(
    navigateOutOfPurchaseFlow: () -> Unit,
    summaryViewModel: SummaryViewModel = hiltViewModel()
) {
    val uiState by summaryViewModel.summaryUiState.collectAsStateWithLifecycle()
    SummaryScreen(
        uiState,
        navigateOutOfPurchaseFlow,
    )
}


