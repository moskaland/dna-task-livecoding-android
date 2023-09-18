package com.devmoskal.core.datasource

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

internal class CartInMemoryDataSource @Inject constructor() : CartDataSource {
    private var _cart: MutableStateFlow<Map<String, Long>> = MutableStateFlow(mapOf())
    override var cart: StateFlow<Map<String, Long>> = _cart

    override fun addToCart(id: String, quantity: Long) {
        val newMap = _cart.value.toMutableMap()
        newMap[id] = (_cart.value[id] ?: 0L) + quantity
        _cart.value = newMap.toMap()
    }

    override fun removeFromCart(id: String, quantity: Long) {
        val newMap = _cart.value.toMutableMap()
        newMap[id].let { currentQuantity ->
            when {
                currentQuantity == null -> return
                currentQuantity <= quantity -> newMap.remove(id)
                else -> newMap[id] = currentQuantity - quantity
            }
        }
        _cart.value = newMap.toMap()
    }

    override fun clear() {
        _cart.value = emptyMap()
    }
}