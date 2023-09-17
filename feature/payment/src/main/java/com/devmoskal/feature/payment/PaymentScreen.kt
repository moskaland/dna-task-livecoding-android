package com.devmoskal.feature.payment

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.devmoskal.core.designsystem.theme.DNATaskAndroidTheme
import com.devmoskal.core.designsystem.theme.MainText
import com.devmoskal.core.designsystem.theme.White

@Composable
fun PaymentScreen(
    uiState: PaymentUiState,
//    onBackPressed: () -> Unit,
//    onPayClick: () -> Unit,
    navigateUp: () -> Boolean,
) {
    BackHandler {
        //TODO
        navigateUp()
        //if (uiState is PaymentUiState.Data) {
        //onBackPressed()
        // }
    }
    when (uiState) {
        is PaymentUiState.Loading -> CircularProgressIndicator(
            modifier = Modifier
                .fillMaxSize(),
            color = MainText,
            backgroundColor = White
        )

        else -> {}
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DNATaskAndroidTheme {
        PaymentScreen(PaymentUiState.Loading, { true })
    }
}