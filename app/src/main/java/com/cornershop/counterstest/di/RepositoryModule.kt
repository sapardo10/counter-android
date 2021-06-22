package com.cornershop.counterstest.di

import com.cornershop.counterstest.datasources.CounterLocalDataSource
import com.cornershop.data.datasources.ICounterLocalDataSource
import com.cornershop.data.datasources.ICounterRemoteDataSource
import com.cornershop.data.repositories.CounterRepository
import com.cornershop.data.repositories.ICounterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideCounterRepository(
        counterLocalDataSource: ICounterLocalDataSource,
        counterRemoteDataSource: ICounterRemoteDataSource
    ): ICounterRepository {
        return CounterRepository(
            localDataSource = counterLocalDataSource,
            remoteDataSource = counterRemoteDataSource
        )
    }
}