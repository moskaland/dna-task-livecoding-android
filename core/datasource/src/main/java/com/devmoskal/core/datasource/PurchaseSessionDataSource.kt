package com.devmoskal.core.datasource

import com.devmoskal.core.model.PurchaseSessionData
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex

interface PurchaseSessionDataSource {
    val data: StateFlow<PurchaseSessionData?>
    val mutex: Mutex
    suspend fun setData(sessionData: PurchaseSessionData)
    suspend fun clear()
}