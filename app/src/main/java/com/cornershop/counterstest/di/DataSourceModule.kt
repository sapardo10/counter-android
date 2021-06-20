package com.cornershop.counterstest.di

import com.cornershop.counterstest.datasources.CounterLocalDataSource
import com.cornershop.counterstest.datasources.CounterRemoteDataSource
import com.cornershop.counterstest.network.CounterApi
import com.cornershop.data.datasources.ICounterLocalDataSource
import com.cornershop.data.datasources.ICounterRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    fun provideCounterLocalDataSource(
            counterApi: CounterApi
    ): ICounterLocalDataSource {
        return CounterLocalDataSource(
                counterApi = counterApi
        )
    }

    @Provides
    fun provideCounterRemoteDataSource(): ICounterRemoteDataSource {
        return CounterRemoteDataSource()
    }
}