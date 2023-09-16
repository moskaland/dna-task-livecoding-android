package com.devmoskal.core.network.mock

import com.devmoskal.core.network.PaymentApiClient
import com.devmoskal.core.network.model.PaymentRequest
import com.devmoskal.core.network.model.PaymentResponse
import com.devmoskal.core.network.model.PaymentStatus
import kotlinx.coroutines.delay
import javax.inject.Inject

internal class MockPaymentApiClient @Inject constructor() : PaymentApiClient {
    /**
     * Call this method to execute payment on the account connected with provided card token
     */
    override suspend fun pay(paymentRequest: PaymentRequest): PaymentResponse {
        delay(2500)

        return if (paymentRequest.currency == "EUR" && paymentRequest.amount >= 20.00) {
            PaymentResponse(paymentRequest.transactionID, PaymentStatus.SUCCESS)
        } else {
            PaymentResponse(paymentRequest.transactionID, PaymentStatus.FAILED)
        }
    }

    /**
     * Method meant for reverting payment when transaction could not be completed by the merchant.
      */
    override suspend fun revert(paymentRequest: PaymentRequest): PaymentResponse {
        delay(500)
        return if (paymentRequest.amount >= 1.00 ) {
            PaymentResponse(paymentRequest.transactionID, PaymentStatus.SUCCESS)
        } else {
            PaymentResponse(paymentRequest.transactionID, PaymentStatus.FAILED)
        }
    }
}