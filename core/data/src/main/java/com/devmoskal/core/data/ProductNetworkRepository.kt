package com.devmoskal.core.data

import com.devmoskal.core.common.di.DefaultDispatcher
import com.devmoskal.core.common.di.IoDispatcher
import com.devmoskal.core.model.Product
import com.devmoskal.core.model.Quantity
import com.devmoskal.core.network.PurchaseApiClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class ProductNetworkRepository @Inject constructor(
    private val purchaseApiClient: PurchaseApiClient,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ProductRepository {
    override suspend fun getProducts(): List<Product> = purchaseApiClient.getProducts()


    override suspend fun calculateTotalValue(order: Map<String, Quantity>): Double {
        // This piece of logic should most likely be done on backend side
        val products = withContext(ioDispatcher) {
            purchaseApiClient.getProducts()
        }
        return withContext(defaultDispatcher) {
            order.map { entry ->
                val orderedProduct = products.first { product -> product.productID == entry.key }

                if (entry.value <= 0) {
                    throw Exception("Not allowed to order not positive number of items")
                }

                entry.value * orderedProduct.unitNetValue * (1.0 + orderedProduct.tax)
            }.sum()
        }
    }
}