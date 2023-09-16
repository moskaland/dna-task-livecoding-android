package com.devmoskal.core.data.di

import com.devmoskal.core.data.CartInMemoryRepository
import com.devmoskal.core.data.CartRepository
import com.devmoskal.core.data.ProductNetworkRepository
import com.devmoskal.core.data.ProductRepository
import com.devmoskal.core.data.PurchaseInMemoryRepository
import com.devmoskal.core.data.PurchaseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.migration.DisableInstallInCheck

@Module(includes = [InternalDataModule::class])
@InstallIn(SingletonComponent::class)
object DataModule

@Module
@DisableInstallInCheck
internal interface InternalDataModule {
    @Binds
    fun bindsCartRepository(cartInMemoryRepository: CartInMemoryRepository): CartRepository

    @Binds
    fun bindsProductRepository(productNetworkRepository: ProductNetworkRepository): ProductRepository

    @Binds
    fun bindsPurchaseRepository(purchaseInMemoryRepository: PurchaseInMemoryRepository): PurchaseRepository
}