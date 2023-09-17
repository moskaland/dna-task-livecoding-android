package com.devmoskal.feature.purchase

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devmoskal.core.designsystem.theme.DNATaskAndroidTheme
import com.devmoskal.core.designsystem.theme.Gray
import com.devmoskal.core.designsystem.theme.MainText
import com.devmoskal.core.designsystem.theme.White
import com.devmoskal.core.designsystem.theme.component.CTAButton
import com.devmoskal.core.designsystem.theme.component.ErrorDialog

@Composable
fun PurchaseScreen(
    uiState: PurchaseUiState,
    onBackPressed: () -> Unit,
    onPayClick: () -> Unit,
    navigateUp: () -> Boolean,
) {
    BackHandler {
        if (uiState is PurchaseUiState.Data) {
            onBackPressed()
        }
    }
    PurchaseContent(uiState, onPayClick)

    when (uiState) {
        is PurchaseUiState.Cleanup.Finished -> navigateUp()
        is PurchaseUiState.Cleanup.Error -> {
            Toast.makeText(
                LocalContext.current,
                stringResource(R.string.purchase_cancellation_error),
                Toast.LENGTH_LONG
            )
                .show()
            navigateUp()
        }

        is PurchaseUiState.Error -> ErrorDialog(
            message = R.string.purchase_error_message,
            buttonText = R.string.purchase_go_back,
            closeAction = onBackPressed,
        )

        else -> {}
    }
}

@Composable
internal fun PurchaseContent(uiState: PurchaseUiState, onPayClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .focusable(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(uiState)
        CTAButton(textIdRes = R.string.purchase_finalize) {
            onPayClick()
        }
    }
}

@Composable
private fun Header(uiState: PurchaseUiState) {
    Row(
        Modifier
            .background(Gray)
            .fillMaxWidth()
            .height(150.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (uiState is PurchaseUiState.Data) {
            Text(
                fontSize = 22.sp,
                // currently pluralStringResource supports only int, conversion itemCount.toInt() is a shortcut done in interview/MVP scope
                text = pluralStringResource(
                    id = R.plurals.purchase_order_description,
                    count = uiState.itemCount.toInt(),
                    uiState.itemCount.toInt()
                )
            )
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (uiState is PurchaseUiState.Cleanup) {
                    Text(text = stringResource(R.string.purchase_canceling_order), Modifier.padding(bottom = 8.dp))
                }
                LinearProgressIndicator(
                    modifier = Modifier
                        .width(256.dp),
                    color = MainText,
                    backgroundColor = White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DNATaskAndroidTheme {
        PurchaseScreen(PurchaseUiState.Data(5L), {}, {}, { true })
    }
}