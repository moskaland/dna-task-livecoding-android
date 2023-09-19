package com.devmoskal.feature.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmoskal.core.data.PaymentRepository
import com.devmoskal.core.data.model.PaymentError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val paymentRepository: PaymentRepository,
) : ViewModel() {
    private val _paymentUiState = MutableStateFlow<PaymentUiState>(PaymentUiState.Idle)
    val paymentUiState: StateFlow<PaymentUiState> = _paymentUiState.asStateFlow()

    fun pay() {
        _paymentUiState.value = PaymentUiState.Processing
        viewModelScope.launch {
            paymentRepository.pay()
                .onSuccess {
                    _paymentUiState.value = PaymentUiState.Complete
                }
                .onFailure(::handlePaymentError)
        }
    }

    private fun handlePaymentError(paymentError: PaymentError) {
        _paymentUiState.value = PaymentUiState.Error(
            when (paymentError) {
                PaymentError.InternalPaymentError -> PaymentUiError.PAYMENT_ERROR
                PaymentError.GeneralCardReaderError,
                PaymentError.KnownCardReaderError -> PaymentUiError.CARD_ERROR

                PaymentError.Canceled,
                PaymentError.RefundError,
                PaymentError.TransactionNotFound -> PaymentUiError.GENERAL_ERROR
            }
        )
    }

    fun onErrorAcknowledgeByUser() {
        // here can goes max retry logic etc.
        _paymentUiState.value = PaymentUiState.Idle
    }
}

sealed interface PaymentUiState {
    object Idle : PaymentUiState
    object Complete : PaymentUiState
    object Processing : PaymentUiState
    data class Error(val type: PaymentUiError) : PaymentUiState
}

enum class PaymentUiError {
    CARD_ERROR,
    PAYMENT_ERROR,
    GENERAL_ERROR,
}