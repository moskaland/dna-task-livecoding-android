package com.devmoskal.core.designsystem.theme.component

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.devmoskal.core.designsystem.theme.Black
import com.devmoskal.core.designsystem.theme.White

@Composable
fun CTAButton(
    modifier: Modifier = Modifier,
    @StringRes textIdRes: Int,
    onClick: () -> Unit,
) {
    Row(
        modifier
            .background(White)
            .fillMaxWidth()
            .height(50.dp)
            .clickable {
                onClick()
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(textIdRes), color = Black)
    }
}