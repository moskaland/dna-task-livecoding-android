package com.devmoskal.core.datasource

import com.devmoskal.core.model.TransactionSessionData
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex

interface TransactionSessionDataSource {
    val data: StateFlow<TransactionSessionData?>
    val mutex: Mutex
    suspend fun setData(sessionData: TransactionSessionData)
    suspend fun clear()
}