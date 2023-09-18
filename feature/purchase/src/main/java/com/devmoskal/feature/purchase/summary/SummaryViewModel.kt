package com.devmoskal.feature.purchase.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmoskal.core.data.PurchaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SummaryViewModel @Inject constructor(
    private val purchaseRepository: PurchaseRepository,
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
                    _summaryUiState.value = SummaryUiState.Error
                }
        }
    }
}

sealed interface SummaryUiState {
    object Success : SummaryUiState
    object Loading : SummaryUiState
    object Error : SummaryUiState

}