package io.dnatechnology.dnataskandroid.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.devmoskal.core.designsystem.theme.DNATaskAndroidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RootComposeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DNATaskAndroidTheme {
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = com.devmoskal.core.designsystem.theme.MainBackground
                ) {
                    RootNavHost(navController = navController)
                }
            }
        }
    }
}
