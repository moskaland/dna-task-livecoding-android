package com.devmoskal.core.datasource

import com.devmoskal.core.model.Transaction
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class TransactionInMemoryDataSourceTest {

    private lateinit var transactionInMemoryDataSource: TransactionInMemoryDataSource

    @Before
    fun setUp() {
        transactionInMemoryDataSource = TransactionInMemoryDataSource()
    }

    @Test
    fun `when setting a transaction, it should be reflected in data source`() = runTest {
        // Given
        val transaction = Transaction("id", mockk(), mockk())

        // When
        transactionInMemoryDataSource.setTransaction(transaction)

        // Then
        assertThat(transactionInMemoryDataSource.transaction.first()).isEqualTo(transaction)
    }

    @Test
    fun `when data source is initialized, it should be null`() = runTest {
        // Given
        // When
        transactionInMemoryDataSource.clear()

        // Then
        assertThat(transactionInMemoryDataSource.transaction.first()).isNull()
    }

    @Test
    fun `when clearing transaction it should be null`() = runTest {
        // Given
        transactionInMemoryDataSource.setTransaction(mockk())

        // When
        transactionInMemoryDataSource.clear()

        // Then
        assertThat(transactionInMemoryDataSource.transaction.first()).isNull()
    }
}