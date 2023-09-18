package com.devmoskal.core.data

import com.devmoskal.core.model.Quantity
import kotlinx.coroutines.flow.StateFlow

interface CartRepository {
    val cart: StateFlow<Map<String, Quantity>>
    fun addToCart(id: String, quantity: Long = 1)
    fun removeFromCart(id: String, quantity: Long = 1)
    fun clear()
    /**
     * Calculate total deductible amount of items in the cart.
     * @return Total order value in EUR; multiple currency is not supported;
     */
    suspend fun calculateTotalValue(): Double
}