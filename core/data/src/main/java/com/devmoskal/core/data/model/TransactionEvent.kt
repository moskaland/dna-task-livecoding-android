package com.devmoskal.core.data.model

import com.devmoskal.core.model.Transaction

sealed class TransactionEvent {
    data class Initialize(val transaction: Transaction, val totalValue: Double) : TransactionEvent()
    data class Pay(val cardToken: String) : TransactionEvent()
    object Confirm : TransactionEvent()
    object Cancel : TransactionEvent()
    object Refund : TransactionEvent()
    object TransactionFail : TransactionEvent()
}

object InvalidTransitionException : Exception()