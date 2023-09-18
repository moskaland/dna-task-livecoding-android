package com.devmoskal.core.data.model

import com.devmoskal.core.model.Transaction

sealed class PurchaseEvent {
    data class Initialize(val transaction: Transaction) : PurchaseEvent()
    data class Pay(val cardToken: String) : PurchaseEvent()
    object Confirm : PurchaseEvent()
    object Cancel : PurchaseEvent()
    object Refund : PurchaseEvent()
    object TransactionFail : PurchaseEvent()
}

object InvalidTransitionException : Exception()