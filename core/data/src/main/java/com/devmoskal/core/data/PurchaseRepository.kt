package com.devmoskal.core.data

interface PurchaseRepository {
    suspend fun initiatePurchaseTransaction(order: Map<String, Long>): com.devmoskal.core.common.Result<Unit, PurchaseErrors>
}