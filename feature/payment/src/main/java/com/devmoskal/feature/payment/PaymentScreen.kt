package com.devmoskal.feature.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devmoskal.core.designsystem.theme.DNATaskAndroidTheme
import com.devmoskal.core.designsystem.theme.Gray
import com.devmoskal.core.designsystem.theme.MainText
import com.devmoskal.core.designsystem.theme.White

@Composable
fun PaymentScreen(
    uiState: PaymentUiState
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            Modifier
                .background(Gray)
                .fillMaxWidth()
                .height(150.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (uiState) {
                is PaymentUiState.Loading -> LinearProgressIndicator(
                    modifier = Modifier
                        .width(256.dp),
                    color = MainText,
                    backgroundColor = White
                )

                is PaymentUiState.PurchaseInfo -> {
                    Text(
                        fontSize = 22.sp,
                        // currently pluralStringResource supports only int, conversion itemCount.toInt() is a shortcut done in interview/MVP scope
                        text = pluralStringResource(
                            id = R.plurals.payment_order_description,
                            count = uiState.itemCount.toInt(),
                            uiState.itemCount.toInt()
                        )
                    )
                }


                else -> {}
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DNATaskAndroidTheme {
        PaymentScreen(PaymentUiState.PurchaseInfo(5L))
    }
}