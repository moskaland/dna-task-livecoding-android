package com.devmoskal.feature.purchase.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmoskal.core.data.PaymentRepository
import com.devmoskal.core.data.PurchaseRepository
import com.devmoskal.core.data.model.PurchaseErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SummaryViewModel @Inject constructor(
    private val purchaseRepository: PurchaseRepository,
    private val paymentRepository: PaymentRepository,
) : ViewModel() {

    private val _summaryUiState = MutableStateFlow<SummaryUiState>(SummaryUiState.Loading)
    val summaryUiState: StateFlow<SummaryUiState> = _summaryUiState.asStateFlow()

    init {
        viewModelScope.launch {
            purchaseRepository.finalizeTransaction()
                .onSuccess {
                    _summaryUiState.value = SummaryUiState.Success
                }
                .onFailure {
                    when (it) {
                        is PurchaseErrors.UnableToConfirmTransaction -> refund()
                        else -> _summaryUiState.value = SummaryUiState.Error(wasRefunded = false)
                    }
                }
        }
    }

    private suspend fun refund() {
        paymentRepository.refund()
            .onSuccess {
                _summaryUiState.value = SummaryUiState.Error(wasRefunded = true)
            }
            .onFailure {
                _summaryUiState.value = SummaryUiState.Error(wasRefunded = false)
            }
    }
}

sealed interface SummaryUiState {
    object Success : SummaryUiState
    object Loading : SummaryUiState
    data class Error(val wasRefunded: Boolean) : SummaryUiState
}