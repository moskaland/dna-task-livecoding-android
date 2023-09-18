package com.devmoskal.core.datasource

import com.devmoskal.core.model.TransactionSessionData
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class PurchaseSessionInMemoryDataSourceTest {

    private lateinit var sessionInMemoryDataSource: TransactionSessionInMemoryDataSource

    @Before
    fun setUp() {
        sessionInMemoryDataSource = TransactionSessionInMemoryDataSource()
    }

    @Test
    fun `when setting a transaction, it should be reflected in data source`() = runTest {
        // Given
        val transaction = TransactionSessionData("id", mockk(), mockk(), 0.0)

        // When
        sessionInMemoryDataSource.setData(transaction)

        // Then
        assertThat(sessionInMemoryDataSource.data.first()).isEqualTo(transaction)
    }

    @Test
    fun `when data source is initialized, it should be null`() = runTest {
        // Given
        // When
        sessionInMemoryDataSource.clear()

        // Then
        assertThat(sessionInMemoryDataSource.data.first()).isNull()
    }

    @Test
    fun `when clearing transaction it should be null`() = runTest {
        // Given
        sessionInMemoryDataSource.setData(mockk())

        // When
        sessionInMemoryDataSource.clear()

        // Then
        assertThat(sessionInMemoryDataSource.data.first()).isNull()
    }
}