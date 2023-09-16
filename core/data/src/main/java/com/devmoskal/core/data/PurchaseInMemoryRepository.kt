package com.devmoskal.core.data

import com.devmoskal.core.common.Result
import com.devmoskal.core.common.di.IoDispatcher
import com.devmoskal.core.network.PurchaseApiClient
import com.devmoskal.core.network.model.PurchaseRequest
import com.devmoskal.core.network.model.PurchaseResponse
import com.devmoskal.core.network.model.TransactionStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class PurchaseInMemoryRepository @Inject constructor(
    private val purchaseApiClient: PurchaseApiClient,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : PurchaseRepository {

    private var transactionData: TransactionData? = null

    override suspend fun initiatePurchaseTransaction(order: Map<String, Long>): Result<Unit, PurchaseErrors> {
        if (transactionData != null) {
            return Result.Failure(PurchaseErrors.AnotherTransactionInProgressError)
        }
        return withContext(ioDispatcher) {
            val response = purchaseApiClient.initiatePurchaseTransaction(PurchaseRequest(order))
            if (response.transactionStatus == TransactionStatus.INITIATED) {
                transactionData = response.toTransactionData()
                Result.Success(Unit)
            } else {
                Result.Failure(PurchaseErrors.GeneralError)
            }
        }
    }

    private fun PurchaseResponse.toTransactionData() = TransactionData(transactionID, transactionStatus, order)

    data class TransactionData(
        val transactionID: String,
        val status: TransactionStatus,
        val order: Map<String, Long>,
    )
}



