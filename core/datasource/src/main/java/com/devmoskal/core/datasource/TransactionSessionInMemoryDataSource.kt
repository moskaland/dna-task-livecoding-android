package com.devmoskal.core.datasource

import com.devmoskal.core.model.TransactionSessionData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import javax.inject.Inject

internal class TransactionSessionInMemoryDataSource @Inject constructor() : TransactionSessionDataSource {
    private val _data = MutableStateFlow<TransactionSessionData?>(null)
    override val data = _data.asStateFlow()

    override val mutex: Mutex = Mutex()

    override suspend fun setData(sessionData: TransactionSessionData) {
        _data.value = sessionData
    }

    override suspend fun clear() {
        _data.value = null
    }
}