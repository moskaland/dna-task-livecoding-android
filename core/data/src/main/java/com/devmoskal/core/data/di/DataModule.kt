package com.devmoskal.core.data.di

import com.devmoskal.core.data.CartOfflineRepository
import com.devmoskal.core.data.CartRepository
import com.devmoskal.core.data.ProductNetworkRepository
import com.devmoskal.core.data.ProductRepository
import com.devmoskal.core.data.PurchaseInMemoryRepository
import com.devmoskal.core.data.PurchaseRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.migration.DisableInstallInCheck
import kotlinx.coroutines.sync.Mutex
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [InternalRepositoryModule::class, InternalDataModule::class])
@InstallIn(SingletonComponent::class)
object DataModule

@Module
@DisableInstallInCheck
internal interface InternalRepositoryModule {
    @Binds
    fun bindsCartRepository(cartOfflineRepository: CartOfflineRepository): CartRepository

    @Binds
    fun bindsProductRepository(productNetworkRepository: ProductNetworkRepository): ProductRepository

    @Binds
    fun bindsPurchaseRepository(purchaseInMemoryRepository: PurchaseInMemoryRepository): PurchaseRepository
}

@Module
@DisableInstallInCheck
internal object InternalDataModule {
    @Provides
    @Singleton
    @Named("TransactionMutex")
    fun provideTransactionMutex() = Mutex()
}