package com.devmoskal.feature.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmoskal.core.network.PurchaseApiClient
import com.devmoskal.core.network.mock.MockPurchaseApiClient

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductsViewModel: ViewModel() {
    val purchaseApiClient: PurchaseApiClient = MockPurchaseApiClient()

    private var mutableCart: MutableStateFlow<Set<String>> = MutableStateFlow(setOf())
    var cart: StateFlow<Set<String>> = mutableCart

    private var mutableProducts = MutableStateFlow<List<com.devmoskal.core.model.Product>?>(null)
    var products: StateFlow<List<com.devmoskal.core.model.Product>?> = mutableProducts

    fun getProducts() {
        viewModelScope.launch {
            mutableProducts.value = purchaseApiClient.getProducts()
        }
    }


    fun addToCart(productID: String) {
        val newSet = mutableCart.value.toMutableSet()
        newSet.add(productID)
        mutableCart.value = newSet.toSet()
    }

    fun removeFromCart(productID: String) {
        val newSet = mutableCart.value.toMutableSet()
        if (newSet.contains(productID)) {
            newSet.remove(productID)
        }
        mutableCart.value = newSet.toSet()
    }
}