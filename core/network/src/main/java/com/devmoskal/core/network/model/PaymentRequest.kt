package com.devmoskal.core.network.model

data class PaymentRequest(val transactionID: String, val amount: Double, val currency: String, val cardToken: String)

data class PaymentResponse(val transactionID: String, val status: PaymentStatus)

enum class PaymentStatus {
    SUCCESS,
    FAILED
}
