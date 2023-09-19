package com.devmoskal.core.network

import com.devmoskal.core.model.Product
import com.devmoskal.core.network.model.PurchaseCancelRequest
import com.devmoskal.core.network.model.PurchaseConfirmRequest
import com.devmoskal.core.network.model.PurchaseRequest
import com.devmoskal.core.network.model.PurchaseResponse
import com.devmoskal.core.network.model.PurchaseStatusResponse

interface PurchaseApiClient {
    suspend fun getProducts(): List<Product>

    suspend fun initiatePurchaseTransaction(purchaseRequest: PurchaseRequest): PurchaseResponse

    suspend fun confirm(purchaseRequest: PurchaseConfirmRequest): PurchaseStatusResponse

    suspend fun cancel(purchaseRequest: PurchaseCancelRequest): PurchaseStatusResponse
}