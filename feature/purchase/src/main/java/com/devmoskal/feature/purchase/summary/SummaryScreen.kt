package com.devmoskal.feature.purchase.summary

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devmoskal.core.designsystem.theme.DNATaskAndroidTheme
import com.devmoskal.core.designsystem.theme.MainText
import com.devmoskal.core.designsystem.theme.White
import com.devmoskal.core.designsystem.theme.component.CTAButton
import com.devmoskal.core.designsystem.theme.component.ErrorDialog
import com.devmoskal.feature.purchase.R

@Composable
fun SummaryScreen(
    uiState: SummaryUiState,
    navigateOutOfPurchaseFlow: () -> Unit,
) {
    BackHandler {
        if (uiState is SummaryUiState.Success) {
            navigateOutOfPurchaseFlow()
        }
    }
    when (uiState) {
        is SummaryUiState.Loading -> Loading()
        is SummaryUiState.Success -> SuccessContent(navigateOutOfPurchaseFlow)
        is SummaryUiState.Error -> ErrorDialog(uiState, navigateOutOfPurchaseFlow)
    }
}

@Composable
private fun ErrorDialog(
    uiState: SummaryUiState.Error,
    navigateOutOfPurchaseFlow: () -> Unit
) {
    ErrorDialog(
        title = R.string.purchase_error_title_not_confirmed,
        message = if (uiState.wasRefunded) {
            R.string.purchase_error_message_not_confirmed_refunded
        } else {
            R.string.purchase_error_message_not_confirmed_not_refunded
        },
        buttonText = R.string.purchase_acknowledge,
        closeAction = navigateOutOfPurchaseFlow,
    )
}

@Composable
private fun Loading() {
    CircularProgressIndicator(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
            .size(100.dp),
        color = MainText,
        backgroundColor = White
    )
}

@Composable
internal fun SuccessContent(navigateOutOfPurchaseFlow: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .focusable(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            fontSize = 22.sp,
            color = MainText,
            // currently pluralStringResource supports only int, conversion itemCount.toInt() is a shortcut done in interview/MVP scope
            text = stringResource(R.string.purchase_transaction_ok)
        )
        CTAButton(textIdRes = R.string.purchase_done) {
            navigateOutOfPurchaseFlow()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DNATaskAndroidTheme {
        SummaryScreen(SummaryUiState.Success) {}
    }
}