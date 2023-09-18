package com.devmoskal.core.model

data class TransactionSessionData(
    val transactionID: String,
    val status: TransactionStatus,
    val order: Map<String, Long>,
    val totalValue: Double,
    val currency: String = "EUR", // fixed in MVP
    val paymentInfo: PaymentInfo = PaymentInfo.Unpaid,
)

sealed interface PaymentInfo {
    object Unpaid : PaymentInfo
    data class Paid(val cardToken: String) : PaymentInfo
    object Refunded : PaymentInfo
}