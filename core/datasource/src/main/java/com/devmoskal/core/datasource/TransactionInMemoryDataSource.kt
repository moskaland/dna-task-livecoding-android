package com.devmoskal.core.datasource

import com.devmoskal.core.model.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

internal class TransactionInMemoryDataSource @Inject constructor() : TransactionDataSource {

    private val _transaction = MutableStateFlow<Transaction?>(null)
    override val transaction = _transaction.asStateFlow()

    override suspend fun setTransaction(transaction: Transaction) {
        _transaction.value = transaction
    }

    override suspend fun clear() {
        _transaction.value = null
    }

    override suspend fun markAsPaid() {
        _transaction.value = _transaction.value?.copy(isPaid = true)
    }
}