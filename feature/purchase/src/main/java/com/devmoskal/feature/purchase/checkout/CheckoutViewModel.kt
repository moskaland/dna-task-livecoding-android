package com.devmoskal.feature.purchase.checkout

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
class CheckoutViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val purchaseRepository: PurchaseRepository,
) : ViewModel() {

    private val _checkoutUiState = MutableStateFlow<CheckoutUiState>(CheckoutUiState.Loading)
    val checkoutUiState: StateFlow<CheckoutUiState> = _checkoutUiState.asStateFlow()

    init {
        viewModelScope.launch {
            val order = cartRepository.cart.value
            purchaseRepository.initiateTransaction(order)
                .onSuccess {
                    _checkoutUiState.value = CheckoutUiState.Data(order.values.sum())
                }
                .onFailure {
                    _checkoutUiState.value = CheckoutUiState.Error
                }
        }
    }

    /**
     * Cancel any ongoing transaction
     */
    fun cleanup() {
        _checkoutUiState.value = CheckoutUiState.Cleanup.Processing
        viewModelScope.launch {
            purchaseRepository.cancelOngoingTransaction()
                .onSuccess {
                    _checkoutUiState.value = CheckoutUiState.Cleanup.Finished
                }
                .onFailure {
                    // error during cancellation or during error handling itself
                    // I assume it is out of scope of interview task, yet it's important case
                    _checkoutUiState.value = CheckoutUiState.Cleanup.Error
                }
        }
    }
}

sealed interface CheckoutUiState {
    data class Data(val itemCount: Long) : CheckoutUiState
    object Loading : CheckoutUiState
    object Error : CheckoutUiState

    sealed interface Cleanup : CheckoutUiState {
        object Processing : Cleanup
        object Finished : Cleanup
        object Error : Cleanup
    }
}