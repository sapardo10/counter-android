package com.cornershop.counterstest.di

import android.content.Context
import androidx.room.Room
import com.cornershop.counterstest.database.AppDatabase
import com.cornershop.counterstest.database.daos.CounterDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "CounterDatabase"
        ).build()
    }

    @Provides
    fun provideCounterDAO(appDatabase: AppDatabase): CounterDAO {
        return appDatabase.counterDao()
    }
}