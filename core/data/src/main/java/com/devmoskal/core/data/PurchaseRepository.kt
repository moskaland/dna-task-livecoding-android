package com.devmoskal.core.data

import com.devmoskal.core.common.Result
import com.devmoskal.core.data.model.PurchaseErrors

interface PurchaseRepository {
    suspend fun initiateTransaction(order: Map<String, Long>): Result<Unit, PurchaseErrors>
    suspend fun finalizeTransaction(): Result<Unit, PurchaseErrors>
    suspend fun cancelOngoingTransaction(): Result<Unit, PurchaseErrors>
}