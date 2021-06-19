package com.cornershop.counterstest.network

import com.cornershop.counterstest.network.dtos.CounterDTO
import retrofit2.http.GET

interface CounterApi {
    @GET("v1/counters")
    suspend fun getCounters(): List<CounterDTO>
}