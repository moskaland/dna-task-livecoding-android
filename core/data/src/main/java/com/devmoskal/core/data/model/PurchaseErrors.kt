package com.devmoskal.core.data.model

sealed class PurchaseErrors {
    object AnotherTransactionInProgressError : PurchaseErrors()
    object GeneralError : PurchaseErrors()
    object UnableToCancelTransaction : PurchaseErrors()
    data class UnableToConfirmTransaction(val wasRefunded: Boolean) : PurchaseErrors()
    object TransactionNotFound : PurchaseErrors()
}
