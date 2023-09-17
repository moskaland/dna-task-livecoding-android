package com.devmoskal.feature.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
) : ViewModel() {
    private val _paymentUiState = MutableStateFlow<PaymentUiState>(PaymentUiState.Idle)
    val paymentUiState: StateFlow<PaymentUiState> = _paymentUiState.asStateFlow()

    fun pay() {
        _paymentUiState.value = PaymentUiState.Processing(ProcessingState.CARD)
        viewModelScope.launch {
            delay(2000)
            _paymentUiState.value = PaymentUiState.Processing(ProcessingState.PAYMENT)
            delay(2000)
            _paymentUiState.value = PaymentUiState.Complete
        }
    }

    fun onErrorAcknowledge() {
        // here can goes max retry logic etc
        _paymentUiState.value = PaymentUiState.Idle
    }
}

sealed interface PaymentUiState {
    object Idle : PaymentUiState
    object Complete : PaymentUiState
    data class Processing(val state: ProcessingState) : PaymentUiState
    data class Error(val type: PaymentErrors) : PaymentUiState
}

enum class ProcessingState {
    CARD,
    PAYMENT,
}

enum class PaymentErrors {
    CARD_ERROR,
    PAYMENT_ERROR,
}