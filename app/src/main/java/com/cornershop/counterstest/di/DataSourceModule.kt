package com.cornershop.counterstest.di

import com.cornershop.counterstest.database.daos.CounterDAO
import com.cornershop.counterstest.datasources.CounterLocalDataSource
import com.cornershop.counterstest.datasources.CounterRemoteDataSource
import com.cornershop.counterstest.network.apis.CounterApi
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
        counterDAO: CounterDAO
    ): ICounterLocalDataSource {
        return CounterLocalDataSource(
            counterDAO = counterDAO
        )
    }

    @Provides
    fun provideCounterRemoteDataSource(
        counterApi: CounterApi
    ): ICounterRemoteDataSource {
        return CounterRemoteDataSource(
            counterApi = counterApi
        )
    }
}