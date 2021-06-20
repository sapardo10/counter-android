package com.cornershop.counterstest.datasources

import com.cornershop.counterstest.network.CounterApi
import com.cornershop.data.datasources.ICounterLocalDataSource

class CounterLocalDataSource(
        private val counterApi: CounterApi
): ICounterLocalDataSource {

    override suspend fun getAll() {
        val result = counterApi.getCounters()
    }
}