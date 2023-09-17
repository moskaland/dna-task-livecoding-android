package com.devmoskal.core.data.model

sealed class PaymentError {
    object KnownCardReaderError : PaymentError()
    object GeneralCardReaderError : PaymentError()
    object InternalPaymentError : PaymentError()
    object TransactionNotFound : PaymentError()
    object Canceled : PaymentError()
}
