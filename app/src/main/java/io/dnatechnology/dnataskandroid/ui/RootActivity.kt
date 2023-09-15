package io.dnatechnology.dnataskandroid.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.devmoskal.core.designsystem.theme.DNATaskAndroidTheme
import com.devmoskal.feature.products.ProductsModel
import com.devmoskal.feature.products.ProductsScreen

class RootComposeActivity : ComponentActivity() {

    private val productsModel: ProductsModel by viewModels<ProductsModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DNATaskAndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = com.devmoskal.core.designsystem.theme.MainBackground
                ) {
                    ProductsScreen(productsModel = productsModel)
                }
            }
        }
    }
}
