package io.dnatechnology.dnataskandroid.ui.viewbased

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import io.dnatechnology.dnataskandroid.R
import io.dnatechnology.dnataskandroid.ui.api.Product
import io.dnatechnology.dnataskandroid.ui.viewmodel.ProductsModel

class RootViewActivity : AppCompatActivity() {

    private lateinit var productsModel: ProductsModel

    private lateinit var adapter: ArrayAdapter<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root_view)

        productsModel = ViewModelProvider(this).get(ProductsModel::class.java)
        adapter = ArrayAdapter<Product>(this, R.layout.product_item)

        lifecycleScope.launchWhenStarted {
            productsModel.products.collect { data ->
                for (product in data ?: listOf())
                    adapter.add(product)
            }
        }


        val listView = findViewById<ListView>(R.id.product_list)
        listView.adapter = adapter

        productsModel.getProducts()
    }
}