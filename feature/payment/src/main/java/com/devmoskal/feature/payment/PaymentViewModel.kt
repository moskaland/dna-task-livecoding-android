package com.devmoskal.feature.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmoskal.core.data.CartRepository
import com.devmoskal.core.data.PurchaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val purchaseRepository: PurchaseRepository,
) : ViewModel() {

    private val _paymentUiState = MutableStateFlow<PaymentUiState>(PaymentUiState.Loading)
    val paymentUiState: StateFlow<PaymentUiState> = _paymentUiState.asStateFlow()

    init {
        viewModelScope.launch {
            val order = cartRepository.cart.value
            purchaseRepository.initiateTransaction(order)
                .onSuccess {
                    _paymentUiState.value = PaymentUiState.PurchaseInfo(order.values.sum())
                }
                .onFailure {
                    _paymentUiState.value = PaymentUiState.Error
                }
        }
    }

    /**
     * Cancel any ongoing transaction
     */
    fun pay() {
        _paymentUiState.value = PaymentUiState.Cleanup.Processing
        viewModelScope.launch {
            purchaseRepository.cancelOngoingTransaction()
                .onSuccess {
                    _paymentUiState.value = PaymentUiState.Cleanup.Finished
                }
                .onFailure {
                    // error during cancellation or during error handling itself
                    // I assume it is out of scope of interview task, yet it's important case
                    _paymentUiState.value = PaymentUiState.Cleanup.Error
                }
        }
    }

    /**
     * Cancel any ongoing transaction
     */
    fun cleanup() {
        _paymentUiState.value = PaymentUiState.Cleanup.Processing
        viewModelScope.launch {
            purchaseRepository.cancelOngoingTransaction()
                .onSuccess {
                    _paymentUiState.value = PaymentUiState.Cleanup.Finished
                }
                .onFailure {
                    // error during cancellation or during error handling itself
                    // I assume it is out of scope of interview task, yet it's important case
                    _paymentUiState.value = PaymentUiState.Cleanup.Error
                }
        }
    }
}

sealed interface PaymentUiState {
    data class PurchaseInfo(val itemCount: Long) : PaymentUiState
    object Loading : PaymentUiState
    object Error : PaymentUiState

    sealed interface Cleanup : PaymentUiState {
        object Processing : Cleanup
        object Finished : Cleanup
        object Error : Cleanup
    }
}