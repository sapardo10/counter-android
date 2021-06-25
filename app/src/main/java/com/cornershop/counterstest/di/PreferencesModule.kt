package com.cornershop.counterstest.di

import android.content.Context
import com.cornershop.counterstest.session.IUserPreferencesHelper
import com.cornershop.counterstest.session.UserPreferencesHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Provides
    fun provideSharedPreferencesHelper(
        @ApplicationContext context: Context
    ): IUserPreferencesHelper {
        return UserPreferencesHelper(
            context = context
        )
    }
}