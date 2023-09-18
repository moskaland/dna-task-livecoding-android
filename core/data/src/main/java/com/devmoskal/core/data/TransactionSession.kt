package com.devmoskal.core.data

import com.devmoskal.core.common.Result
import com.devmoskal.core.data.model.InvalidTransitionException
import com.devmoskal.core.data.model.TransactionEvent
import com.devmoskal.core.model.TransactionSessionData
import kotlinx.coroutines.flow.StateFlow

interface TransactionSession {
    val state: StateFlow<TransactionSessionData?>
    suspend fun process(event: TransactionEvent): Result<Unit, InvalidTransitionException>
    suspend fun clear()
}






