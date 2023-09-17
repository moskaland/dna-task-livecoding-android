package com.devmoskal.core.datasource

import com.devmoskal.core.model.Transaction
import kotlinx.coroutines.flow.StateFlow

interface TransactionDataSource {
    val transaction: StateFlow<Transaction?>
    suspend fun setTransaction(transaction: Transaction)
    suspend fun markAsPaid()
    suspend fun clear()
}