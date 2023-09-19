package com.devmoskal.core.data.model

sealed class PaymentError {
    object KnownCardReaderError : PaymentError()
    object GeneralCardReaderError : PaymentError()
    object InternalPaymentError : PaymentError()
    object TransactionNotFound : PaymentError()
    object RefundError : PaymentError()
    object Canceled : PaymentError()
}
