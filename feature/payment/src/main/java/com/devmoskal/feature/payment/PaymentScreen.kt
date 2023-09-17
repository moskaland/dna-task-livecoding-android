package com.devmoskal.feature.payment

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.devmoskal.core.designsystem.theme.DNATaskAndroidTheme
import com.devmoskal.core.designsystem.theme.Gray
import com.devmoskal.core.designsystem.theme.MainText
import com.devmoskal.core.designsystem.theme.White

@Composable
fun PaymentScreen(
    uiState: PaymentUiState,
    onBackPressed: () -> Unit,
    navigateUp: () -> Boolean,
) {
    BackHandler {
        if (uiState is PaymentUiState.PurchaseInfo) {
            onBackPressed()
        }
    }
    PaymentContent(uiState)

    when (uiState) {
        is PaymentUiState.Cleanup.Finished -> navigateUp()
        is PaymentUiState.Cleanup.Error -> {
            Toast.makeText(LocalContext.current, stringResource(R.string.payment_cancellation_error), Toast.LENGTH_LONG)
                .show()
            navigateUp()
        }

        is PaymentUiState.Error -> ErrorDialog(onBackPressed)
        else -> {}
    }
}

@Composable
internal fun PaymentContent(uiState: PaymentUiState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .focusable(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PaymentHeader(uiState)
    }
}

@Composable
private fun PaymentHeader(uiState: PaymentUiState) {
    Row(
        Modifier
            .background(Gray)
            .fillMaxWidth()
            .height(150.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (uiState is PaymentUiState.PurchaseInfo) {
            Text(
                fontSize = 22.sp,
                // currently pluralStringResource supports only int, conversion itemCount.toInt() is a shortcut done in interview/MVP scope
                text = pluralStringResource(
                    id = R.plurals.payment_order_description,
                    count = uiState.itemCount.toInt(),
                    uiState.itemCount.toInt()
                )
            )
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (uiState is PaymentUiState.Cleanup) {
                    Text(text = stringResource(R.string.payment_canceling_order), Modifier.padding(bottom = 8.dp))
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

@Composable
fun ErrorDialog(onBackPressed: () -> Unit) {
    val configuration = LocalConfiguration.current
    /*
     * Bizarre settings of this dialog can be removed when there's fix to
     * https://issuetracker.google.com/issues/221643630
     */
    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 80.dp),
        onDismissRequest = { },
        title = {
            Text(
                text = stringResource(R.string.payment_error_title),
                style = MaterialTheme.typography.h5,
            )
        },
        text = {
            Divider()
            Text(
                text = stringResource(R.string.payment_error_message),
                modifier = Modifier.padding(vertical = 16.dp),
            )
        },
        confirmButton = {
            Text(
                text = stringResource(R.string.payment_go_back),
                style = MaterialTheme.typography.button,
                color = MainText,
                modifier = Modifier
                    .padding(all = 16.dp)
                    .clickable { onBackPressed() },
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
fun DialogPreview() {
    DNATaskAndroidTheme {
        ErrorDialog { true }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DNATaskAndroidTheme {
        PaymentScreen(PaymentUiState.PurchaseInfo(5L), {}, { true })
    }
}