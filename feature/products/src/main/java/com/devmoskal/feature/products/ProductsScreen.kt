package com.devmoskal.feature.products

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devmoskal.core.designsystem.theme.Black
import com.devmoskal.core.designsystem.theme.DNATaskAndroidTheme
import com.devmoskal.core.designsystem.theme.Gray
import com.devmoskal.core.designsystem.theme.White
import com.devmoskal.core.model.Product

@Composable
fun ProductsScreen(
    products: List<Product>?,
    cart: Set<String>,
    getProducts: () -> Unit,
    addToCart: (String) -> Unit,
    removeFromCart: (String) -> Unit,
    onPayClick: () -> Unit
) {
    LaunchedEffect(Unit) {
        getProducts()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (products != null) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                for (product in products) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 8.dp)
                            .background(if (cart.contains(product.productID)) Gray else White)
                            .border(1.dp, Black)
                            .clickable {
                                if (cart.contains(product.productID)) {
                                    removeFromCart(product.productID)
                                } else {
                                    addToCart(product.productID)
                                }
                            }
                    ) {
                        Text(
                            text = product.toString(),
                            color = Black,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .padding(vertical = 8.dp)
                        )
                    }
                }
            }
        } else {
            Text(text = "LOADING")
        }


        Row(
            Modifier
                .background(White)
                .fillMaxWidth()
                .height(50.dp)
                .clickable {
                    onPayClick()
                },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.products_pay), color = Black)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DNATaskAndroidTheme {
        ProductsScreen(
            products = emptyList(),
            cart = emptySet(),
            getProducts = {},
            addToCart = {},
            removeFromCart = {},
            onPayClick = { },
        )
    }
}