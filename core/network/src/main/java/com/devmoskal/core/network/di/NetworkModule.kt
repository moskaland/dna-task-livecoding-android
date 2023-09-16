package com.devmoskal.core.network.di

import com.devmoskal.core.network.PaymentApiClient
import com.devmoskal.core.network.PurchaseApiClient
import com.devmoskal.core.network.mock.MockPaymentApiClient
import com.devmoskal.core.network.mock.MockPurchaseApiClient
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.migration.DisableInstallInCheck

@Module(includes = [InternalNetworkModule::class])
@InstallIn(SingletonComponent::class)
object NetworkModule

@Module
@DisableInstallInCheck
internal interface InternalNetworkModule {
    @Binds
    fun bindsPaymentApiClient(mockPaymentApiClient: MockPaymentApiClient): PaymentApiClient

    @Binds
    fun bindsPurchaseApiClient(mockPurchaseApiClient: MockPurchaseApiClient): PurchaseApiClient
}