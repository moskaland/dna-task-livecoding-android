package com.devmoskal.feature.payment

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
) : ViewModel() {

    private val _paymentUiState = MutableStateFlow<PaymentUiState>(PaymentUiState.Loading)
    val paymentUiState: StateFlow<PaymentUiState> = _paymentUiState.asStateFlow()
}

sealed interface PaymentUiState {
    object Data : PaymentUiState
    object Loading : PaymentUiState
    object Error : PaymentUiState
}