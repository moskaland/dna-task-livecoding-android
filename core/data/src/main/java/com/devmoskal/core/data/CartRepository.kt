package com.devmoskal.core.data

import com.devmoskal.core.model.Quantity
import kotlinx.coroutines.flow.StateFlow

interface CartRepository {
    val cart: StateFlow<Map<String, Quantity>>
    fun addToCart(id: String, quantity: Long = 1)
    fun removeFromCart(id: String, quantity: Long = 1)
    fun clear()
}