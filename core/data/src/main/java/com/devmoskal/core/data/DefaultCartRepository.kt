package com.devmoskal.core.data

import com.devmoskal.core.common.di.DefaultDispatcher
import com.devmoskal.core.common.di.IoDispatcher
import com.devmoskal.core.datasource.CartDataSource
import com.devmoskal.core.model.Quantity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class DefaultCartRepository @Inject constructor(
    private val cartDataSource: CartDataSource,
    private val productRepository: ProductRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : CartRepository {

    override var cart: StateFlow<Map<String, Quantity>> = cartDataSource.cart

    override fun addToCart(id: String, quantity: Long) = cartDataSource.addToCart(id, quantity)

    override fun removeFromCart(id: String, quantity: Long) = cartDataSource.removeFromCart(id, quantity)

    override fun clear() {
        cartDataSource.clear()
    }

    override suspend fun calculateTotalValue(): Double {
        val products = withContext(ioDispatcher) {
            productRepository.getProducts()
        }
        return withContext(defaultDispatcher) {
            cartDataSource.cart.value.map { entry ->
                val orderedProduct = products.first { product -> product.productID == entry.key }

                if (entry.value <= 0) {
                    throw Exception("Not allowed to order not positive number of items")
                }

                entry.value * orderedProduct.unitNetValue * (1.0 + orderedProduct.tax)
            }.sum()
        }
    }
}