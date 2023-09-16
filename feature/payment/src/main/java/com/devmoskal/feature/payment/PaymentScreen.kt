package com.devmoskal.feature.payment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devmoskal.core.designsystem.theme.DNATaskAndroidTheme
import com.devmoskal.core.designsystem.theme.Gray
import com.devmoskal.core.designsystem.theme.MainText

@Composable
fun PaymentScreen(
    uiState: PaymentUiState
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            is PaymentUiState.Loading -> LinearProgressIndicator(
                modifier = Modifier.width(64.dp),
                color = MainText,
                backgroundColor = Gray
            )

            is PaymentUiState.PurchaseInfo -> {
                // currently pluralStringResource supports only int, conversion itemCount.toInt() is a shortcut done in interview/MVP scope
                Text(
                    text = pluralStringResource(
                        id = R.plurals.payment_order_description,
                        count = uiState.itemCount.toInt()
                    )
                )
            }

            else -> {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DNATaskAndroidTheme {
        PaymentScreen(PaymentUiState.Loading)
    }
}