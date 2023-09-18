package com.devmoskal.core.data

import android.util.Log
import com.devmoskal.core.common.Result
import com.devmoskal.core.common.di.IoDispatcher
import com.devmoskal.core.data.model.PurchaseErrors
import com.devmoskal.core.data.model.TransactionEvent
import com.devmoskal.core.model.PaymentInfo
import com.devmoskal.core.model.Transaction
import com.devmoskal.core.model.TransactionStatus
import com.devmoskal.core.network.PurchaseApiClient
import com.devmoskal.core.network.model.PurchaseCancelRequest
import com.devmoskal.core.network.model.PurchaseConfirmRequest
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
    private val session: TransactionSession,
    private val cartRepository: CartRepository,
    @Named("SessionMutex") private val mutex: Mutex,
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
            val transaction = session.state.value ?: return Result.Failure(PurchaseErrors.TransactionNotFound)

            val response =
                purchaseApiClient.confirm(PurchaseConfirmRequest(transaction.order, transaction.transactionID))
            return if (response.status == TransactionStatus.CONFIRMED) {
                session.clear()
                cartRepository.clear()
                Result.Success
            } else {
                // TODO refund
                Result.Failure(PurchaseErrors.GeneralError)
            }
        }
    }

    override suspend fun cancelOngoingTransaction(): Result<Unit, PurchaseErrors> {
        mutex.withLock {
            val sessionData = session.state.value
            if (sessionData?.paymentInfo is PaymentInfo.Paid) {
                // TODO refund
            }
            return when {
                sessionData == null -> Result.Success
                sessionData.status == TransactionStatus.INITIATED -> {
                    performCancellationApiCall(sessionData.transactionID)
                }

                else -> {
                    session.clear()
                    Result.Success
                }
            }
        }
    }

    private fun isOngoingTransaction() = session.state.value != null

    private suspend fun performInitialApiCall(order: Map<String, Long>): Result<Unit, PurchaseErrors> =
        withContext(ioDispatcher) {
            val response = purchaseApiClient.initiatePurchaseTransaction(PurchaseRequest(order))
            if (response.transactionStatus == TransactionStatus.INITIATED) {
                session.process(
                    TransactionEvent.Initialize(
                        response.toTransaction(),
                        cartRepository.calculateTotalValue()
                    )
                )
                Result.Success
            } else {
                Result.Failure(PurchaseErrors.GeneralError)
            }
        }

    private suspend fun performCancellationApiCall(transactionId: String): Result<Unit, PurchaseErrors> =
        withContext(ioDispatcher) {
            val response = purchaseApiClient.cancel(PurchaseCancelRequest(transactionId))
            if (response.status == TransactionStatus.CANCELLED) {
                session.clear() // MVP do not process CANCELLED state
                Result.Success
            } else {
                Result.Failure(PurchaseErrors.UnableToCancelTransaction)
            }
        }

    private fun PurchaseResponse.toTransaction() = Transaction(transactionID, transactionStatus, order)
}
