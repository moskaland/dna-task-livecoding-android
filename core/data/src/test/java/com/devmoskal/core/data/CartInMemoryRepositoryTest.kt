package com.devmoskal.core.data

import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class CartInMemoryRepositoryTest {

    @Test
    fun `when adding an item to cart, it should be in the cart`() = runTest {
        // Given
        val repo = CartInMemoryRepository()

        // When
        repo.addToCart("item1", 1L)

        // Then
        val firstItem = repo.cart.value
        assertThat(firstItem["item1"]).isEqualTo(1L)
    }

    @Test
    fun `when removing an item from cart, it should not be in the cart`() = runTest {
        // Given
        val repo = CartInMemoryRepository()
        repo.addToCart("item1", 1L)

        // When
        repo.removeFromCart("item1", 1L)

        // Then
        val cartItems = repo.cart.value
        assertThat(cartItems).doesNotContainKey("item1")
    }

    @Test
    fun `when adding multiple items to cart, they should be in the cart`() = runTest {
        // Given
        val repo = CartInMemoryRepository()

        // When
        repo.addToCart("item1", 1L)
        repo.addToCart("item2", 1L)
        repo.addToCart("item3", 1L)

        // Then
        val cartItems = repo.cart.value
        assertThat(cartItems).containsEntry("item1", 1L)
        assertThat(cartItems).containsEntry("item2", 1L)
        assertThat(cartItems).containsEntry("item2", 1L)
    }

    @Test
    fun `when adding the same item multiple times, its count should increase`() = runTest {
        // Given
        val repo = CartInMemoryRepository()

        // When
        repo.addToCart("item1", 1L)
        repo.addToCart("item1", 1L)

        // Then
        val cartItems = repo.cart.value
        assertThat(cartItems["item1"]).isEqualTo(2L)
    }

    @Test
    fun `when cart is empty, it should return an empty map`() = runTest {
        // Given
        val repo = CartInMemoryRepository()

        // When
        val cartItems = repo.cart.value

        // Then
        assertThat(cartItems).isEmpty()
    }

    @Test
    fun `when removing an item that doesn't exist, cart should remain unchanged`() = runTest {
        // Given
        val repo = CartInMemoryRepository()
        repo.addToCart("item1", 1L)

        // When
        repo.removeFromCart("item2", 1L)

        // Then
        val cartItems = repo.cart.value
        assertThat(cartItems).containsEntry("item1", 1L)
    }

    @Test
    fun `when removing an item multiple times, its count should decrease`() = runTest {
        // Given
        val repo = CartInMemoryRepository()
        repo.addToCart("item1", 1L)
        repo.addToCart("item1", 1L)

        // When
        repo.removeFromCart("item1", 1L)
        repo.removeFromCart("item1", 1L)

        // Then
        val cartItems = repo.cart.value
        assertThat(cartItems).doesNotContainKey("item1")
    }

    @Test
    fun `when adding an item with a quantity greater than 1, the quantity should be updated`() = runTest {
        // Given
        val repo = CartInMemoryRepository()

        // When
        repo.addToCart("item1", 5L)

        // Then
        val cartItems = repo.cart.value
        assertThat(cartItems["item1"]).isEqualTo(5L)
    }

    @Test
    fun `when removing a quantity greater than the current quantity, the item should be removed`() = runTest {
        // Given
        val repo = CartInMemoryRepository()
        repo.addToCart("item1", 2L)

        // When
        repo.removeFromCart("item1", 5L)

        // Then
        val cartItems = repo.cart.value
        assertThat(cartItems).doesNotContainKey("item1")
    }

    @Test
    fun `when removing a quantity of 0, the cart should remain unchanged`() = runTest {
        // Given
        val repo = CartInMemoryRepository()
        repo.addToCart("item1", 1L)

        // When
        repo.removeFromCart("item1", 0L)

        // Then
        val cartItems = repo.cart.value
        assertThat(cartItems).containsEntry("item1", 1L)
    }

    @Test
    fun `when performing multiple operation, the cart should reflect all operations`() = runTest {
        val repo = CartInMemoryRepository()
        assertThat(repo.cart.value).isEmpty()

        repo.addToCart("item1", 2L)
        assertThat(repo.cart.value).containsEntry("item1", 2L)


        repo.addToCart("item2", 3L)
        assertThat(repo.cart.value).containsEntry("item2", 3L)


        repo.removeFromCart("item1", 2L)
        assertThat(repo.cart.value).doesNotContainKey("item1")

        repo.addToCart("item2", 5L)
        assertThat(repo.cart.value).containsEntry("item2", 8L)
    }
}