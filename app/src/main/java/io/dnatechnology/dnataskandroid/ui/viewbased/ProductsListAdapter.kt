package io.dnatechnology.dnataskandroid.ui.viewbased

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.dnatechnology.dnataskandroid.R
import io.dnatechnology.dnataskandroid.ui.api.Product

class ProductsListAdapter(private val products: List<Product>) :
    RecyclerView.Adapter<ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        val product = products[position]
        holder.productLabel.text = product.toString()

    }

    override fun getItemCount(): Int {
        return products.size
    }

}

class ProductViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    val productLabel: TextView = itemView.findViewById(R.id.product_label)
}