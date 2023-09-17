package com.devmoskal.core.data

import android.util.Log
import com.devmoskal.core.common.Result
import com.devmoskal.core.datasource.TransactionDataSource
import com.devmoskal.core.model.Transaction
import com.devmoskal.core.model.TransactionStatus
import com.devmoskal.core.network.PurchaseApiClient
import com.devmoskal.core.network.model.PurchaseResponse
import com.devmoskal.core.network.model.PurchaseStatusResponse
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PurchaseInMemoryRepositoryTest {
    private val purchaseApiClient: PurchaseApiClient = mockk()
    private val transactionDataSource: TransactionDataSource = mockk()
    private val dispatcher: CoroutineDispatcher = StandardTestDispatcher()
    private val purchaseInMemoryRepository =
        PurchaseInMemoryRepository(purchaseApiClient, transactionDataSource, Mutex(), dispatcher)

    @Test
    fun `when initiating purchase with no ongoing transaction, start transaction and return success`() =
        runTest(dispatcher) {
            // Given
            val purchaseResponse = PurchaseResponse(mockk(), "transactionID", TransactionStatus.INITIATED)
            coEvery { purchaseApiClient.initiatePurchaseTransaction(any()) } returns purchaseResponse
            coEvery { transactionDataSource.transaction } returns flowOf(null).stateIn(this)
            coEvery { transactionDataSource.setTransaction(any()) } just Runs

            // When
            val result = purchaseInMemoryRepository.initiateTransaction(mockk())

            // Then
            verify { transactionDataSource.setTransaction(any()) }
        assertThat(result).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun `when initiating purchase while another transaction is in progress than request should fail`() =
        runTest(dispatcher) {
            // Given
            mockkStatic(Log::class) // Tiber should be used for logging, not done due to time constrains
            every { Log.e(any(), any()) } returns 0
            val purchaseResponse = PurchaseResponse(mockk(), "transactionID", TransactionStatus.INITIATED)
            coEvery { purchaseApiClient.initiatePurchaseTransaction(any()) } returns purchaseResponse
            coEvery { transactionDataSource.transaction } returns flowOf<Transaction>(mockk()).stateIn(this)

            // When
            purchaseInMemoryRepository.initiateTransaction(mockk())
            val result = purchaseInMemoryRepository.initiateTransaction(mockk())


            // Then
            assertThat(result).isInstanceOf(Result.Failure::class.java)
            assertThat((result as Result.Failure).error).isEqualTo(PurchaseErrors.AnotherTransactionInProgressError)
    }

    @Test
    fun `when initiating purchase with API failure than should return general api error`() = runTest(dispatcher) {
        // Given
        val failedPurchaseResponse = PurchaseResponse(mockk(), "transactionID", TransactionStatus.FAILED)
        coEvery { purchaseApiClient.initiatePurchaseTransaction(any()) } returns failedPurchaseResponse
        coEvery { transactionDataSource.transaction } returns flowOf(null).stateIn(this)
        coEvery { transactionDataSource.setTransaction(any()) } just Runs

        // When
        val result = purchaseInMemoryRepository.initiateTransaction(mockk())

        // Then
        assertThat(result).isInstanceOf(Result.Failure::class.java)
        assertThat((result as Result.Failure).error).isEqualTo(PurchaseErrors.GeneralError)
    }

    @Test
    fun `when cancelling transaction with no ongoing transaction, should return success`() = runTest(dispatcher) {
        // Given
        coEvery { transactionDataSource.transaction.value } returns null

        // When
        val result = purchaseInMemoryRepository.cancelOngoingTransaction()

        // Then
        assertThat(result).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun `when cancelling transaction in INITIATED state and cancellation API succeed then return success`() =
        runTest(dispatcher) {
            // Given
            val ongoingTransaction = Transaction("transactionID", TransactionStatus.INITIATED, mockk())
            coEvery { transactionDataSource.transaction.value } returns ongoingTransaction
            coEvery { purchaseApiClient.cancel(any()) } returns PurchaseStatusResponse(
                "transactionID",
                TransactionStatus.CANCELLED
            )
            coEvery { transactionDataSource.clear() } just Runs

            // When
            val result = purchaseInMemoryRepository.cancelOngoingTransaction()

            // Then
            coVerify { purchaseApiClient.cancel(any()) }
            coVerify { transactionDataSource.clear() }
            assertThat(result).isInstanceOf(Result.Success::class.java)
        }

    @Test
    fun `when cancelling transaction in INITIATED state and cancellation API failed then return failure`() =
        runTest(dispatcher) {
            // Given
            val ongoingTransaction = Transaction("transactionID", TransactionStatus.INITIATED, mockk())
            coEvery { transactionDataSource.transaction.value } returns ongoingTransaction
            coEvery { purchaseApiClient.cancel(any()) } returns PurchaseStatusResponse(
                "transactionID",
                TransactionStatus.FAILED
            )
            coEvery { transactionDataSource.clear() } just Runs

            // When
            val result = purchaseInMemoryRepository.cancelOngoingTransaction()

            // Then
            coVerify { purchaseApiClient.cancel(any()) }
            coVerify(inverse = true) { transactionDataSource.clear() }
            assertThat(result).isInstanceOf(Result.Failure::class.java)
        }

    @Test
    fun `when cancelling transaction in state other than INITIATED then do not call cancellation API yet clear transaction and return success`() =
        runTest(dispatcher) {
            // Given
            val ongoingTransaction = Transaction("transactionID", TransactionStatus.CONFIRMED, mockk())
            coEvery { transactionDataSource.transaction.value } returns ongoingTransaction
            coEvery { purchaseApiClient.cancel(any()) } returns PurchaseStatusResponse(
                "transactionID",
                TransactionStatus.CANCELLED
            )
            coEvery { transactionDataSource.clear() } just Runs

            // When
            val result = purchaseInMemoryRepository.cancelOngoingTransaction()

            // Then
            coVerify(inverse = true) { purchaseApiClient.cancel(any()) }
            coVerify { transactionDataSource.clear() }
            assertThat(result).isInstanceOf(Result.Success::class.java)
        }
}