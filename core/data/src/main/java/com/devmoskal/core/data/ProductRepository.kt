package com.devmoskal.core.data

import com.devmoskal.core.model.Product

interface ProductRepository {
    suspend fun getProducts(): List<Product>
}