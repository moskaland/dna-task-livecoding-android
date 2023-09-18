package com.devmoskal.core.data

import com.devmoskal.core.common.Result
import com.devmoskal.core.data.model.InvalidTransitionException
import com.devmoskal.core.data.model.PurchaseEvent
import com.devmoskal.core.model.PurchaseSessionData
import kotlinx.coroutines.flow.StateFlow

interface PurchaseSession {
    val state: StateFlow<PurchaseSessionData?>
    suspend fun process(event: PurchaseEvent): Result<Unit, InvalidTransitionException>
    suspend fun clear()
}






