package com.devmoskal.core.data
import com.devmoskal.core.datasource.CartDataSource
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

// a bit dummy set of test due to simplicity of Repository
class CartOfflineRepositoryTest {

    private lateinit var cartDataSource: CartDataSource
    private lateinit var cartOfflineRepository: CartOfflineRepository

    @Before
    fun setUp() {
        cartDataSource = mockk(relaxed = true)
        cartOfflineRepository = CartOfflineRepository(cartDataSource)
    }

    @Test
    fun `when adding an item to cart then items should be added into data source`() {
        // Given
        val id = "item1"
        val quantity: Long = 2

        // When
        cartOfflineRepository.addToCart(id, quantity)

        // Then
        verify { cartDataSource.addToCart(id, quantity) }
    }

    @Test
    fun `when removing an item from cart then items should be removed from data source`() {
        // Given
        val id = "item1"
        val quantity: Long = 0

        // When
        cartOfflineRepository.removeFromCart(id, quantity)

        // Then
        verify { cartDataSource.removeFromCart(id, quantity) }
    }


    @Test
    fun `when using cart then data should comes from data source`() {
        // Then
        assertThat(cartOfflineRepository.cart).isEqualTo(cartDataSource.cart)
    }
}