package com.devmoskal.core.data

sealed class PurchaseErrors {

    object AnotherTransactionInProgressError : PurchaseErrors()


}
