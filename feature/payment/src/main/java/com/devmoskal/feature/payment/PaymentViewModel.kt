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
            purchaseRepository.initiatePurchaseTransaction(order)
                .onSuccess {
                    _paymentUiState.value = PaymentUiState.PurchaseInfo(
                        order.values.sum()
                    )
                }
                .onFailure {
                    _paymentUiState.value = PaymentUiState.Error
                }
        }
    }
}

sealed interface PaymentUiState {
    data class PurchaseInfo(val itemCount: Long) : PaymentUiState
    object Loading : PaymentUiState
    object Error : PaymentUiState
}