package com.cornershop.counterstest.network.apis

import com.cornershop.counterstest.network.dtos.CounterDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST

interface CounterApi {
    @POST("v1/counter/dec")
    suspend fun decrementCounter(@Body body: CounterDTO): List<CounterDTO>

    @HTTP(method = "DELETE", path = "v1/counter", hasBody = true)
    suspend fun deleteCounter(@Body body: CounterDTO): List<CounterDTO>

    @GET("v1/counters")
    suspend fun getCounters(): List<CounterDTO>

    @POST("v1/counter/inc")
    suspend fun incrementCounter(@Body body: CounterDTO): List<CounterDTO>
}