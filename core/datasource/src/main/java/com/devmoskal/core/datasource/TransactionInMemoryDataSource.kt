package com.devmoskal.core.datasource

import com.devmoskal.core.model.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

internal class TransactionInMemoryDataSource @Inject constructor() : TransactionDataSource {

    private val _cart = MutableStateFlow<Transaction?>(null)
    override val transaction = _cart.asStateFlow()

    override suspend fun setTransaction(transaction: Transaction) {
        _cart.value = transaction
    }

    override suspend fun clear() {
        _cart.value = null
    }

    override suspend fun markAsPaid() {
        _cart.value = _cart.value?.copy(isPaid = true)
    }
}