package com.devmoskal.core.datasource

import com.devmoskal.core.model.Transaction
import kotlinx.coroutines.flow.StateFlow

interface TransactionDataSource {
    val transaction: StateFlow<Transaction?>
    fun setTransaction(transaction: Transaction)
    fun clear()
}