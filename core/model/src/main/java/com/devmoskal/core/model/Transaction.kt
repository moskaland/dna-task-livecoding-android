package com.devmoskal.core.model

data class Transaction(
    val transactionID: String,
    val status: TransactionStatus,
    val order: Map<String, Long>,
    val isPaid: Boolean = false
)

enum class TransactionStatus {
    INITIATED,
    CONFIRMED,
    CANCELLED,
    FAILED
}
