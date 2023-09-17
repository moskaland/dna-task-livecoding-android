package com.devmoskal.feature.payment

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.devmoskal.core.designsystem.theme.Black
import com.devmoskal.core.designsystem.theme.DNATaskAndroidTheme
import com.devmoskal.core.designsystem.theme.MainText
import com.devmoskal.core.designsystem.theme.White
import com.devmoskal.core.designsystem.theme.component.CTAButton
import com.devmoskal.core.designsystem.theme.component.ErrorDialog

@Composable
fun PaymentScreen(
    uiState: PaymentUiState,
    onPayClick: () -> Unit,
    onErrorAcknowledge: () -> Unit,
    navigateUp: () -> Boolean,
) {
    BackHandler {
        if (uiState is PaymentUiState.Idle) {
            navigateUp()
        }
    }
    when (uiState) {
        is PaymentUiState.Idle -> CTAButton(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
                .height(150.dp)
                .padding(horizontal = 50.dp),
            textIdRes = R.string.payment_tap_card,
            onClick = onPayClick
        )

        is PaymentUiState.Processing -> ProcessingIndicator(uiState.state)
        is PaymentUiState.Complete -> navigateUp()
        is PaymentUiState.Error -> ErrorDialog(
            message = when (uiState.type) {
                PaymentErrors.CARD_ERROR -> R.string.payment_card_error
                PaymentErrors.PAYMENT_ERROR -> R.string.payment_payment_error
            },
            closeAction = onErrorAcknowledge
        )
    }
}

@Composable
fun ProcessingIndicator(state: ProcessingState) {
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
                .size(100.dp),
            color = MainText,
            backgroundColor = White
        )
        Text(
            text = stringResource(
                when (state) {
                    ProcessingState.CARD -> R.string.payment_processing_card
                    ProcessingState.PAYMENT -> R.string.payment_processing_payment
                }
            ), color = Black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DNATaskAndroidTheme {
        PaymentScreen(PaymentUiState.Idle, {}, {}, { true })
    }
}