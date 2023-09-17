package com.devmoskal.core.data

import android.util.Log
import com.devmoskal.core.common.Result
import com.devmoskal.core.common.di.IoDispatcher
import com.devmoskal.core.data.model.PurchaseErrors
import com.devmoskal.core.datasource.TransactionDataSource
import com.devmoskal.core.model.Transaction
import com.devmoskal.core.model.TransactionStatus
import com.devmoskal.core.network.PurchaseApiClient
import com.devmoskal.core.network.model.PurchaseCancelRequest
import com.devmoskal.core.network.model.PurchaseConfirmRequest
import com.devmoskal.core.network.model.PurchaseRequest
import com.devmoskal.core.network.model.PurchaseResponse
import com.devmoskal.core.service.cardReader.CardReaderService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

internal class PurchaseInMemoryRepository @Inject constructor(
    private val purchaseApiClient: PurchaseApiClient,
    private val transactionDataSource: TransactionDataSource,
    private val cardReaderService: CardReaderService,
    @Named("TransactionMutex") private val mutex: Mutex,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : PurchaseRepository {

    override suspend fun initiateTransaction(order: Map<String, Long>): Result<Unit, PurchaseErrors> {
        mutex.withLock {
            if (isOngoingTransaction()) {
                Log.e("PurchaseRepository", "Corrupted state, there should NOT be more than one ongoing payment flow!")
                return Result.Failure(PurchaseErrors.AnotherTransactionInProgressError)
            }
            return performInitialApiCall(order)
        }
    }

    override suspend fun finalizeTransaction(): Result<Unit, PurchaseErrors> {
        mutex.withLock {
            val transaction =
                transactionDataSource.transaction.value ?: return Result.Failure(PurchaseErrors.TransactionNotFound)

            val response =
                purchaseApiClient.confirm(PurchaseConfirmRequest(transaction.order, transaction.transactionID))
            return if (response.status == TransactionStatus.CONFIRMED) {
                transactionDataSource.clear()
                Result.Success(Unit)
            } else {
                Result.Failure(PurchaseErrors.GeneralError)
            }
        }
    }

    override suspend fun cancelOngoingTransaction(): Result<Unit, PurchaseErrors> {
        mutex.withLock {
            val ongoingTransaction = transactionDataSource.transaction.value
            if (ongoingTransaction?.isPaid == true) {
                // TODO refund
            }
            return when {
                ongoingTransaction == null -> {
                    Result.Success(Unit)
                }
                ongoingTransaction.status == TransactionStatus.INITIATED -> {
                    performCancellationApiCall(ongoingTransaction.transactionID)
                }
                else -> {
                    transactionDataSource.clear()
                    Result.Success(Unit)
                }
            }
        }

    }

    private fun isOngoingTransaction() = transactionDataSource.transaction.value != null

    private suspend fun performInitialApiCall(order: Map<String, Long>): Result<Unit, PurchaseErrors> =
        withContext(ioDispatcher) {
            val response = purchaseApiClient.initiatePurchaseTransaction(PurchaseRequest(order))
            if (response.transactionStatus == TransactionStatus.INITIATED) {
                transactionDataSource.setTransaction(response.toTransactionData())
                Result.Success(Unit)
            } else {
                Result.Failure(PurchaseErrors.GeneralError)
            }
        }

    private suspend fun performCancellationApiCall(transactionId: String): Result<Unit, PurchaseErrors> =
        withContext(ioDispatcher) {
            val response = purchaseApiClient.cancel(PurchaseCancelRequest(transactionId))
            if (response.status == TransactionStatus.CANCELLED) {
                transactionDataSource.clear()
                Result.Success(Unit)
            } else {
                Result.Failure(PurchaseErrors.UnableToCancelTransaction)
            }
        }

    private fun PurchaseResponse.toTransactionData() = Transaction(transactionID, transactionStatus, order)
}
