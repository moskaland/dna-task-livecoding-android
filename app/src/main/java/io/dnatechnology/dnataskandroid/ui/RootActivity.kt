package io.dnatechnology.dnataskandroid.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.devmoskal.core.designsystem.theme.DNATaskAndroidTheme
import com.devmoskal.feature.products.ProductsViewModel

class RootComposeActivity : ComponentActivity() {

    private val productsViewModel: ProductsViewModel by viewModels<ProductsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DNATaskAndroidTheme {
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = com.devmoskal.core.designsystem.theme.MainBackground
                ) {
                    RootNavHost(navController = navController, productsViewModel = productsViewModel)
                }
            }
        }
    }
}
