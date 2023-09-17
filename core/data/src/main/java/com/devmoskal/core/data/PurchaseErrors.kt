package com.devmoskal.core.data

sealed class PurchaseErrors {

    object AnotherTransactionInProgressError : PurchaseErrors()
    object GeneralError : PurchaseErrors()
    object UnableToCancelTransaction : PurchaseErrors()


}
