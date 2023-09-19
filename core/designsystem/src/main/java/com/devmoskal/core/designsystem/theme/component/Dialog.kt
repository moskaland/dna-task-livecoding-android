package com.devmoskal.core.designsystem.theme.component

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.devmoskal.core.designsystem.R
import com.devmoskal.core.designsystem.theme.DNATaskAndroidTheme
import com.devmoskal.core.designsystem.theme.MainText


@Composable
fun ErrorDialog(
    modifier: Modifier = Modifier,
    @StringRes title: Int = R.string.design_system_error_dialog_title,
    @StringRes message: Int = R.string.design_system_error_dialog_message,
    @StringRes buttonText: Int = R.string.design_system_error_dialog_cta,
    isDismissable: Boolean = false,
    closeAction: () -> Unit,
) {
    val configuration = LocalConfiguration.current
    /*
     * Bizarre settings of this dialog can be removed when there's fix to
     * https://issuetracker.google.com/issues/221643630
     */
    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = modifier.widthIn(max = configuration.screenWidthDp.dp - 80.dp),
        onDismissRequest = {
            if (isDismissable) {
                closeAction()
            }
        },
        title = {
            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.h5,
            )
        },
        text = {
            Divider()
            Text(
                text = stringResource(message),
                modifier = Modifier.padding(vertical = 16.dp),
            )
        },
        confirmButton = {
            Text(
                text = stringResource(buttonText),
                style = MaterialTheme.typography.button,
                color = MainText,
                modifier = Modifier
                    .padding(all = 16.dp)
                    .clickable { closeAction() },
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
fun DialogPreview() {
    DNATaskAndroidTheme {
        ErrorDialog { }
    }
}