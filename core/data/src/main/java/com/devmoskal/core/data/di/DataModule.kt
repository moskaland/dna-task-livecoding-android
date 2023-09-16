package com.devmoskal.core.data.di

import com.devmoskal.core.data.CartInMemoryRepository
import com.devmoskal.core.data.CartRepository
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
    fun binds(cartInMemoryRepository: CartInMemoryRepository): CartRepository
}