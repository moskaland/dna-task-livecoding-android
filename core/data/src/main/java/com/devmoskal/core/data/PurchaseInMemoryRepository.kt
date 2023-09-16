package com.devmoskal.core.data

import android.util.Log
import com.devmoskal.core.common.Result
import com.devmoskal.core.common.di.IoDispatcher
import com.devmoskal.core.datasource.TransactionDataSource
import com.devmoskal.core.model.Transaction
import com.devmoskal.core.model.TransactionStatus
import com.devmoskal.core.network.PurchaseApiClient
import com.devmoskal.core.network.model.PurchaseRequest
import com.devmoskal.core.network.model.PurchaseResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

internal class PurchaseInMemoryRepository @Inject constructor(
    private val purchaseApiClient: PurchaseApiClient,
    private val transactionDataSource: TransactionDataSource,
    @Named("TransactionMutex") private val mutex: Mutex,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : PurchaseRepository {


    override suspend fun initiateTransaction(order: Map<String, Long>): Result<Unit, PurchaseErrors> {
        mutex.withLock {
            if (transactionDataSource.transaction.value != null) {
                Log.e("PurchaseRepository", "Corrupted state, there should NOT be more than one ongoing payment flow!")
                return Result.Failure(PurchaseErrors.AnotherTransactionInProgressError)
            }
            return performInitApiCall(order)
        }
    }

    private suspend fun performInitApiCall(order: Map<String, Long>): Result<Unit, PurchaseErrors> =
        withContext(ioDispatcher) {
            val response = purchaseApiClient.initiatePurchaseTransaction(PurchaseRequest(order))
            if (response.transactionStatus == TransactionStatus.INITIATED) {
                transactionDataSource.setTransaction(response.toTransactionData())
                Result.Success(Unit)
            } else {
                Result.Failure(PurchaseErrors.GeneralError)
            }
        }

    private fun PurchaseResponse.toTransactionData() = Transaction(transactionID, transactionStatus, order)
}
