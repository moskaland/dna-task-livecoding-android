package com.devmoskal.core.network.model

import com.devmoskal.core.model.TransactionStatus

data class PurchaseRequest(val order: Map<String, Long>)

data class PurchaseResponse(
    val order: Map<String, Long>,
    val transactionID: String,
    val transactionStatus: TransactionStatus
)

data class PurchaseConfirmRequest(val order: Map<String, Long>, val transactionID: String)

data class PurchaseCancelRequest(val transactionID: String)

data class PurchaseStatusResponse(val transactionID: String, val status: TransactionStatus)


