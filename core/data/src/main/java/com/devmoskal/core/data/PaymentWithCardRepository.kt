package com.devmoskal.core.data

import com.devmoskal.core.common.Result
import com.devmoskal.core.common.di.DefaultDispatcher
import com.devmoskal.core.data.model.PaymentError
import com.devmoskal.core.datasource.TransactionDataSource
import com.devmoskal.core.model.CardData
import com.devmoskal.core.model.Transaction
import com.devmoskal.core.network.PaymentApiClient
import com.devmoskal.core.network.model.PaymentRequest
import com.devmoskal.core.network.model.PaymentStatus
import com.devmoskal.core.service.cardReader.CardReaderService
import com.devmoskal.core.service.cardReader.model.CardReaderException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named


internal class PaymentWithCardRepository @Inject constructor(
    private val productRepository: ProductRepository,
    private val cardReaderService: CardReaderService,
    private val transactionDataSource: TransactionDataSource,
    private val paymentApiClient: PaymentApiClient,
    @Named("TransactionMutex") private val mutex: Mutex,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : PaymentRepository {

    override suspend fun pay(): Result<Unit, PaymentError> = withContext(defaultDispatcher) {
        when (val cardDataResult = readCardData()) {
            is Result.Success -> makePayment(cardDataResult.data)
            is Result.Failure -> cardDataResult
        }
    }

    private suspend fun makePayment(cardData: CardData): Result<Unit, PaymentError> = mutex.withLock {
        val transaction = transactionDataSource.transaction.value
            ?: return Result.Failure(PaymentError.TransactionNotFound)

        val paymentRequest = generatePaymentRequest(cardData, transaction)

        return when (paymentApiClient.pay(paymentRequest).status) {
            PaymentStatus.SUCCESS -> {
                transactionDataSource.markAsPaid()
                Result.Success(Unit)
            }

            PaymentStatus.FAILED -> Result.Failure(PaymentError.InternalPaymentError)
        }
    }

    private suspend fun readCardData(): Result<CardData, PaymentError> {
        return try {
            val cardData = cardReaderService.readCard()
            Result.Success(cardData)
        } catch (cardException: CardReaderException) {
            Result.Failure(PaymentError.KnownCardReaderError)
        } catch (cancellationException: CancellationException) {
            Result.Failure(PaymentError.Canceled)
        } catch (exception: Exception) {
            Result.Failure(PaymentError.GeneralCardReaderError)
        }
    }

    private suspend fun generatePaymentRequest(cardData: CardData, transaction: Transaction) =
        PaymentRequest(
            transaction.transactionID,
            calculateSum(transaction.order),
            transaction.currency,
            cardData.token
        )

    private suspend fun calculateSum(order: Map<String, Long>) = productRepository.calculateTotalValue(order)
}