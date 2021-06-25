package com.cornershop.counterstest.di

import android.content.Context
import com.cornershop.counterstest.database.daos.CounterDAO
import com.cornershop.counterstest.datasources.CounterLocalDataSource
import com.cornershop.counterstest.datasources.CounterRemoteDataSource
import com.cornershop.counterstest.network.apis.CounterApi
import com.cornershop.data.datasources.ICounterLocalDataSource
import com.cornershop.data.datasources.ICounterRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    fun provideCounterLocalDataSource(
        @ApplicationContext context: Context,
        counterDAO: CounterDAO
    ): ICounterLocalDataSource {
        return CounterLocalDataSource(
            context = context,
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