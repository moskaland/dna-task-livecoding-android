package com.devmoskal.core.service.cardReader.di

import com.devmoskal.core.service.cardReader.CardReaderService
import com.devmoskal.core.service.cardReader.mock.MockCardReaderService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.migration.DisableInstallInCheck

@Module(includes = [InternalNetworkModule::class])
@InstallIn(SingletonComponent::class)
object ServiceModule

@Module
@DisableInstallInCheck
internal interface InternalNetworkModule {
    @Binds
    fun bindsCardReaderService(mockCardReaderService: MockCardReaderService): CardReaderService
}