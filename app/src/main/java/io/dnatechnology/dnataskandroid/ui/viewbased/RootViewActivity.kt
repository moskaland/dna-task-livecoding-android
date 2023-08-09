package io.dnatechnology.dnataskandroid.ui.viewbased

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.dnatechnology.dnataskandroid.R
import io.dnatechnology.dnataskandroid.ui.api.Product
import io.dnatechnology.dnataskandroid.ui.viewmodel.ProductsModel
import kotlinx.coroutines.launch

class RootViewActivity : AppCompatActivity() {

    private lateinit var productsModel: ProductsModel

    private lateinit var adapter: ProductsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root_view)

        productsModel = ViewModelProvider(this).get(ProductsModel::class.java)


        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                productsModel.products.collect { data ->
                    adapter = ProductsListAdapter(data ?: listOf())
                    recyclerView.adapter = adapter
                }
            }
        }

        productsModel.getProducts()
    }
}