package com.cornershop.counterstest.datasources

import com.cornershop.counterstest.database.daos.CounterDAO
import com.cornershop.data.datasources.ICounterLocalDataSource
import com.cornershop.data.models.Counter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CounterLocalDataSource(
    private val counterDAO: CounterDAO
) : ICounterLocalDataSource {

    override suspend fun delete(counter: Counter) {
        withContext(Dispatchers.IO) {
            counterDAO.delete(counter.toCounterEntity())
        }
    }

    override suspend fun getAll(): Flow<List<Counter>> {
        return withContext(Dispatchers.IO) {
            counterDAO.getAll().map { entityList ->
                entityList.map {
                    it.toCounter()
                }
            }
        }
    }

    override suspend fun setAll(counters: List<Counter>) {
        withContext(Dispatchers.IO) {
            counterDAO.setAll(
                counters = counters.map {
                    it.toCounterEntity()
                }
            )
        }
    }

}