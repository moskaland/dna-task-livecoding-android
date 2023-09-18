package com.devmoskal.core.data

import com.devmoskal.core.common.Result
import com.devmoskal.core.data.model.PaymentError

interface PaymentRepository {
    suspend fun pay(): Result<Unit, PaymentError>
    suspend fun refund(): Result<Unit, PaymentError>
}