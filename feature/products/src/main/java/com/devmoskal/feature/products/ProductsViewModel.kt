package com.devmoskal.feature.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmoskal.core.data.CartRepository
import com.devmoskal.core.data.ProductRepository
import com.devmoskal.core.model.Quantity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
) : ViewModel() {
    var cart: StateFlow<Map<String, Quantity>> = cartRepository.cart

    private var mutableProducts = MutableStateFlow<List<com.devmoskal.core.model.Product>?>(null)
    var products: StateFlow<List<com.devmoskal.core.model.Product>?> = mutableProducts

    fun getProducts() {
        viewModelScope.launch {
            mutableProducts.value = productRepository.getProducts()
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