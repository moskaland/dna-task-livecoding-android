package com.devmoskal.core.data

import com.devmoskal.core.common.Result
import com.devmoskal.core.common.di.DefaultDispatcher
import com.devmoskal.core.data.model.PaymentError
import com.devmoskal.core.data.model.TransactionEvent
import com.devmoskal.core.model.CardData
import com.devmoskal.core.model.PaymentInfo
import com.devmoskal.core.model.TransactionSessionData
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
    private val cardReaderService: CardReaderService,
    private val session: TransactionSession,
    private val paymentApiClient: PaymentApiClient,
    @Named("SessionMutex") private val mutex: Mutex,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : PaymentRepository {

    override suspend fun pay(): Result<Unit, PaymentError> = withContext(defaultDispatcher) {
        when (val cardDataResult = readCardData()) {
            is Result.Success -> makePayment(cardDataResult.data)
            is Result.Failure -> cardDataResult
        }
    }

    override suspend fun refund(): Result<Unit, PaymentError> = mutex.withLock(defaultDispatcher) {
        val sessionData = session.state.value ?: return Result.Failure(PaymentError.TransactionNotFound)

        val token = (sessionData.paymentInfo as? PaymentInfo.Paid)?.cardToken
            ?: return Result.Failure(PaymentError.RefundError)

        val paymentRequest = generatePaymentRequest(token, sessionData)

        return when (paymentApiClient.revert(paymentRequest).status) {
            PaymentStatus.SUCCESS -> {
                session.process(TransactionEvent.Refund)
                Result.Success
            }

            PaymentStatus.FAILED -> Result.Failure(PaymentError.RefundError)
        }
    }

    private suspend fun makePayment(cardData: CardData): Result<Unit, PaymentError> =
        mutex.withLock(defaultDispatcher) {
            val sessionData = session.state.value ?: return Result.Failure(PaymentError.TransactionNotFound)

            val paymentRequest = generatePaymentRequest(cardData.token, sessionData)

            return when (paymentApiClient.pay(paymentRequest).status) {
                PaymentStatus.SUCCESS -> {
                    session.process(TransactionEvent.Pay(cardData.token))
                    Result.Success
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

    private fun generatePaymentRequest(token: String, sessionData: TransactionSessionData) =
        PaymentRequest(
            sessionData.transactionID,
            sessionData.totalValue,
            sessionData.currency,
            token
        )
}