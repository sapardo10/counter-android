package com.cornershop.counterstest.di

import com.cornershop.counterstest.BuildConfig
import com.cornershop.counterstest.network.apis.CounterApi
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideCounterApi(): CounterApi {
        val logging = HttpLoggingInterceptor()
        val logLevel = if(BuildConfig.DEBUG) {
            Level.BODY
        } else {
            Level.NONE
        }
        logging.setLevel(logLevel)
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(CounterApi::class.java)
    }
}