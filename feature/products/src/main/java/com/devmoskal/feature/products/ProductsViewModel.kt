package com.devmoskal.feature.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmoskal.core.data.CartRepository
import com.devmoskal.core.model.Quantity
import com.devmoskal.core.network.PurchaseApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val purchaseApiClient: PurchaseApiClient, // TODO Vm should not use api layer
    private val cartRepository: CartRepository,
) : ViewModel() {
    var cart: StateFlow<Map<String, Quantity>> = cartRepository.cart

    private var mutableProducts = MutableStateFlow<List<com.devmoskal.core.model.Product>?>(null)
    var products: StateFlow<List<com.devmoskal.core.model.Product>?> = mutableProducts

    fun getProducts() {
        viewModelScope.launch {
            mutableProducts.value = purchaseApiClient.getProducts()
        }
    }


    fun addToCart(productID: String) {
        viewModelScope.launch {
            cartRepository.addToCart(productID)
        }
    }

    fun removeFromCart(productID: String) {
        viewModelScope.launch {
            cartRepository.removeFromCart(productID)
        }
    }
}