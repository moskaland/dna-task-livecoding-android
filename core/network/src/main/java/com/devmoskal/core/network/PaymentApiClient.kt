package com.devmoskal.core.network

import com.devmoskal.core.network.model.PaymentRequest
import com.devmoskal.core.network.model.PaymentResponse

interface PaymentApiClient {
    /**
     * Call this method to execute payment on the account connected with provided card token
     */
    suspend fun pay(paymentRequest: PaymentRequest): PaymentResponse

    /**
     * Method meant for reverting payment when transaction could not be completed by the merchant.
     */
    suspend fun revert(paymentRequest: PaymentRequest): PaymentResponse
}