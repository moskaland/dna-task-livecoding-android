package com.devmoskal.core.datasource

import com.devmoskal.core.model.Transaction
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class PurchaseSessionInMemoryDataSourceTest {

    private lateinit var purchaseSessionInMemoryDataSource: PurchaseSessionInMemoryDataSource

    @Before
    fun setUp() {
        purchaseSessionInMemoryDataSource = PurchaseSessionInMemoryDataSource()
    }

    @Test
    fun `when setting a transaction, it should be reflected in data source`() = runTest {
        // Given
        val transaction = Transaction("id", mockk(), mockk())

        // When
        purchaseSessionInMemoryDataSource.setTransaction(transaction)

        // Then
        assertThat(purchaseSessionInMemoryDataSource.data.first()).isEqualTo(transaction)
    }

    @Test
    fun `when data source is initialized, it should be null`() = runTest {
        // Given
        // When
        purchaseSessionInMemoryDataSource.clear()

        // Then
        assertThat(purchaseSessionInMemoryDataSource.data.first()).isNull()
    }

    @Test
    fun `when clearing transaction it should be null`() = runTest {
        // Given
        purchaseSessionInMemoryDataSource.setData(mockk())

        // When
        purchaseSessionInMemoryDataSource.clear()

        // Then
        assertThat(purchaseSessionInMemoryDataSource.data.first()).isNull()
    }
}