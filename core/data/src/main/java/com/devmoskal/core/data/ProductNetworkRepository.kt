package com.devmoskal.core.data

import com.devmoskal.core.model.Product
import com.devmoskal.core.network.PurchaseApiClient
import javax.inject.Inject

internal class ProductNetworkRepository @Inject constructor(
    private val purchaseApiClient: PurchaseApiClient
) : ProductRepository {
    override suspend fun getProducts(): List<Product> = purchaseApiClient.getProducts()
}