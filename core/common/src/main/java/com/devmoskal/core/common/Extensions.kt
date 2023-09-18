package com.devmoskal.core.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

public suspend inline fun <T> Mutex.withLock(
    dispatcher: CoroutineDispatcher,
    owner: Any? = null,
    crossinline action: () -> T
): T =
    withContext(dispatcher) {
        withLock(owner, action)
    }
