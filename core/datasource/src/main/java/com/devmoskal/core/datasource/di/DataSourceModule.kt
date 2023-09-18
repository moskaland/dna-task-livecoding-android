package com.devmoskal.core.datasource.di

import com.devmoskal.core.datasource.CartDataSource
import com.devmoskal.core.datasource.CartInMemoryDataSource
import com.devmoskal.core.datasource.PurchaseSessionDataSource
import com.devmoskal.core.datasource.PurchaseSessionInMemoryDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.migration.DisableInstallInCheck
import javax.inject.Singleton

@Module(includes = [InternalDataModule::class])
@InstallIn(SingletonComponent::class)
object DataSourceModule

@Module
@DisableInstallInCheck
internal interface InternalDataModule {
    @Binds
    @Singleton
    fun bindsCartDataSource(cartInMemoryDataSource: CartInMemoryDataSource): CartDataSource

    @Binds
    @Singleton
    fun bindsPurchaseSessionDataSource(purchaseSessionInMemoryDataSource: PurchaseSessionInMemoryDataSource): PurchaseSessionDataSource
}