package com.devmoskal.core.datasource

import com.devmoskal.core.model.PurchaseSessionData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import javax.inject.Inject

internal class PurchaseSessionInMemoryDataSource @Inject constructor() : PurchaseSessionDataSource {
    private val _data = MutableStateFlow<PurchaseSessionData?>(null)
    override val data = _data.asStateFlow()

    override val mutex: Mutex = Mutex()

    override suspend fun setData(sessionData: PurchaseSessionData) {
        _data.value = sessionData
    }

    override suspend fun clear() {
        _data.value = null
    }
}