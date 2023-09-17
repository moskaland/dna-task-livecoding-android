package com.devmoskal.core.data

import com.devmoskal.core.model.Product
import com.devmoskal.core.model.Quantity

interface ProductRepository {
    suspend fun getProducts(): List<Product>

    /**
     * Calculate total deductible amount of order.
     * @return Total order value in EUR; multiple currency is not supported;
     */
    suspend fun calculateTotalValue(order: Map<String, Quantity>): Double
}