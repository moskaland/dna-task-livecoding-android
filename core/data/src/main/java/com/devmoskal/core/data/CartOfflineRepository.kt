package com.devmoskal.core.data

import com.devmoskal.core.datasource.CartDataSource
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

internal class CartOfflineRepository @Inject constructor(
    private val cartDataSource: CartDataSource
) : CartRepository {

    override var cart: StateFlow<Map<String, Long>> = cartDataSource.cart

    override fun addToCart(id: String, quantity: Long) = cartDataSource.addToCart(id, quantity)

    override fun removeFromCart(id: String, quantity: Long) = cartDataSource.removeFromCart(id, quantity)
}