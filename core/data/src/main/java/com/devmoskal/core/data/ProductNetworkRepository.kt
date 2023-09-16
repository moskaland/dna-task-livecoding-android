package com.devmoskal.core.data

import com.devmoskal.core.model.Product
import com.devmoskal.core.network.PurchaseApiClient
import javax.inject.Inject

internal class ProductNetworkRepository @Inject constructor(
    private val purchaseApiClient: PurchaseApiClient,
) : ProductRepository {
    // Should be wrapped with Result or other error handling mechanism, omitted in MVP
    override suspend fun getProducts(): List<Product> = purchaseApiClient.getProducts()
}