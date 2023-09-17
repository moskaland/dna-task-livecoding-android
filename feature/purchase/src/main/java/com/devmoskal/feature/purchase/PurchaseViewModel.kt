package com.devmoskal.feature.purchase

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
class PurchaseViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val purchaseRepository: PurchaseRepository,
) : ViewModel() {

    private val _purchaseUiState = MutableStateFlow<PurchaseUiState>(PurchaseUiState.Loading)
    val purchaseUiState: StateFlow<PurchaseUiState> = _purchaseUiState.asStateFlow()

    init {
        viewModelScope.launch {
            val order = cartRepository.cart.value
            purchaseRepository.initiateTransaction(order)
                .onSuccess {
                    _purchaseUiState.value = PurchaseUiState.Data(order.values.sum())
                }
                .onFailure {
                    _purchaseUiState.value = PurchaseUiState.Error
                }
        }
    }

    /**
     * Cancel any ongoing transaction
     */
    fun cleanup() {
        _purchaseUiState.value = PurchaseUiState.Cleanup.Processing
        viewModelScope.launch {
            purchaseRepository.cancelOngoingTransaction()
                .onSuccess {
                    _purchaseUiState.value = PurchaseUiState.Cleanup.Finished
                }
                .onFailure {
                    // error during cancellation or during error handling itself
                    // I assume it is out of scope of interview task, yet it's important case
                    _purchaseUiState.value = PurchaseUiState.Cleanup.Error
                }
        }
    }
}

sealed interface PurchaseUiState {
    data class Data(val itemCount: Long) : PurchaseUiState
    object Loading : PurchaseUiState
    object Error : PurchaseUiState

    sealed interface Cleanup : PurchaseUiState {
        object Processing : Cleanup
        object Finished : Cleanup
        object Error : Cleanup
    }
}