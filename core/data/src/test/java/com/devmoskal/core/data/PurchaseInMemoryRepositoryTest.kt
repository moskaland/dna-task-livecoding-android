package com.devmoskal.core.data

import com.devmoskal.core.common.Result
import com.devmoskal.core.network.PurchaseApiClient
import com.devmoskal.core.network.model.PurchaseResponse
import com.devmoskal.core.network.model.TransactionStatus
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PurchaseInMemoryRepositoryTest {
    private val purchaseApiClient: PurchaseApiClient = mockk()
    private val dispatcher: CoroutineDispatcher = StandardTestDispatcher()
    private val purchaseInMemoryRepository = PurchaseInMemoryRepository(purchaseApiClient, dispatcher)

    @Test
    fun `when initiating purchase with no ongoing transaction, return succeed`() = runTest(dispatcher) {
        // Given
        val purchaseResponse = PurchaseResponse(mockk(), "transactionID", TransactionStatus.INITIATED)
        coEvery { purchaseApiClient.initiatePurchaseTransaction(any()) } returns purchaseResponse

        // When
        val result = purchaseInMemoryRepository.initiatePurchaseTransaction(mockk())

        // Then
        assertThat(result).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun `when initiating purchase while another transaction is in progress than request should fail`() =
        runTest(dispatcher) {
            // Given
            val purchaseResponse = PurchaseResponse(mockk(), "transactionID", TransactionStatus.INITIATED)
            coEvery { purchaseApiClient.initiatePurchaseTransaction(any()) } returns purchaseResponse

            // When
            purchaseInMemoryRepository.initiatePurchaseTransaction(mockk())
            val result = purchaseInMemoryRepository.initiatePurchaseTransaction(mockk())


            // Then
        assertThat(result).isInstanceOf(Result.Failure::class.java)
        assertThat((result as Result.Failure).error).isEqualTo(PurchaseErrors.AnotherTransactionInProgressError)
    }

    @Test
    fun `when initiating purchase with API failure than should return general api error`() = runTest(dispatcher) {
        // Given
        val failedPurchaseResponse = PurchaseResponse(mockk(), "transactionID", TransactionStatus.FAILED)
        coEvery { purchaseApiClient.initiatePurchaseTransaction(any()) } returns failedPurchaseResponse

        // When
        val result = purchaseInMemoryRepository.initiatePurchaseTransaction(mockk())

        // Then
        assertThat(result).isInstanceOf(Result.Failure::class.java)
        assertThat((result as Result.Failure).error).isEqualTo(PurchaseErrors.GeneralError)
    }
}