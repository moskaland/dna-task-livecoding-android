package com.devmoskal.core.data

import com.devmoskal.core.datasource.CartDataSource
import com.devmoskal.core.model.Quantity
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

internal class DefaultCartRepository @Inject constructor(
    private val cartDataSource: CartDataSource,
    ) : CartRepository {

    override var cart: StateFlow<Map<String, Quantity>> = cartDataSource.cart

    override fun addToCart(id: String, quantity: Long) = cartDataSource.addToCart(id, quantity)

    override fun removeFromCart(id: String, quantity: Long) = cartDataSource.removeFromCart(id, quantity)

    override fun clear() {
        cartDataSource.clear()
    }
}