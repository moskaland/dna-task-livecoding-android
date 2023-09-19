package com.devmoskal.core.data.di

import com.devmoskal.core.data.CartRepository
import com.devmoskal.core.data.DefaultCartRepository
import com.devmoskal.core.data.PaymentRepository
import com.devmoskal.core.data.PaymentWithCardRepository
import com.devmoskal.core.data.ProductNetworkRepository
import com.devmoskal.core.data.ProductRepository
import com.devmoskal.core.data.PurchaseInMemoryRepository
import com.devmoskal.core.data.PurchaseRepository
import com.devmoskal.core.data.StateMachineTransactionSession
import com.devmoskal.core.data.TransactionSession
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
    fun bindsCartRepository(defaultCartRepository: DefaultCartRepository): CartRepository

    @Binds
    fun bindsProductRepository(productNetworkRepository: ProductNetworkRepository): ProductRepository

    @Binds
    fun bindsPurchaseRepository(purchaseInMemoryRepository: PurchaseInMemoryRepository): PurchaseRepository

    @Binds
    fun bindsPaymentRepository(paymentWithCardRepository: PaymentWithCardRepository): PaymentRepository

    @Binds
    @Singleton // Work in progress: stateMachinePurchaseSession is stateful, so needs to be shared
    fun bindsPurchaseSession(stateMachinePurchaseSession: StateMachineTransactionSession): TransactionSession
}

@Module
@DisableInstallInCheck
internal object InternalDataModule {
    @Provides
    @Singleton
    @Named("SessionMutex")
    fun provideTransactionMutex() = Mutex()
}