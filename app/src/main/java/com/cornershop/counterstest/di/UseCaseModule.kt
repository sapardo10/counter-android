package com.cornershop.counterstest.di

import com.cornershop.data.repositories.ICounterRepository
import com.cornershop.domain.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideCreateCounterUseCase(
        counterRepository: ICounterRepository
    ): ICreateCounterUseCase {
        return CreateCounterUseCase(
            counterRepository = counterRepository
        )
    }

    @Provides
    fun provideDecreaseCounterUseCase(
        counterRepository: ICounterRepository
    ): IDecreaseCounterUseCase {
        return DecreaseCounterUseCase(
            counterRepository = counterRepository
        )
    }

    @Provides
    fun provideDeleteCounterUseCase(
        counterRepository: ICounterRepository
    ): IDeleteCounterUseCase {
        return DeleteCounterUseCase(
            counterRepository = counterRepository
        )
    }

    @Provides
    fun provideDeleteMultipleCountesrUseCase(
        counterRepository: ICounterRepository
    ): IDeleteMultipleCounterUseCase {
        return DeleteMultipleCounterUseCase(
            counterRepository = counterRepository
        )
    }

    @Provides
    fun provideGetAllCountersUseCase(
        counterRepository: ICounterRepository
    ): IGetAllCountersUseCase {
        return GetAllCountersUseCase(
            counterRepository = counterRepository
        )
    }

    @Provides
    fun provideIncreaseCounterUseCase(
        counterRepository: ICounterRepository
    ): IIncreaseCounterUseCase {
        return IncreaseCounterUseCase(
            counterRepository = counterRepository
        )
    }

}