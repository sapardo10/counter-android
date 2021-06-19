package com.cornershop.counterstest.datasources

import com.cornershop.data.datasources.ICounterLocalDataSource

class CounterLocalDataSource: ICounterLocalDataSource {
    override fun getAll() {
        println("data source")
    }
}