package com.cornershop.counterstest.datasources

import com.cornershop.counterstest.network.apis.CounterApi
import com.cornershop.data.datasources.ICounterRemoteDataSource
import com.cornershop.data.models.Counter
import com.cornershop.data.models.CounterError
import com.cornershop.data.models.Result
import com.cornershop.data.models.Result.Success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Implementation of [ICounterRemoteDataSource]
 */
class CounterRemoteDataSource constructor(
    private val counterApi: CounterApi
) : ICounterRemoteDataSource {

    /**
     * -------------------------------------- PUBLIC METHODS ---------------------------------------
     */

    override suspend fun decreaseCounter(counter: Counter): Result<List<Counter>> {
        return withContext(Dispatchers.IO) {
            try {
                val counters =
                    counterApi.decrementCounter(counter.toCounterDTO()).map { it.toCounter() }
                Success(
                    data = counters
                )
            } catch (e: Exception) {
                Result.Failure(
                    error = CounterError.NETWORK_ERROR
                )
            }
        }
    }

    override suspend fun deleteCounter(counter: Counter): Result<List<Counter>> {
        return withContext(Dispatchers.IO) {
            try {
                val counters = counterApi.deleteCounter(counter.toCounterDTO()).map { it.toCounter() }
                Success(
                    data = counters
                )
            } catch (e: Exception) {
                Result.Failure(
                    error = CounterError.NETWORK_ERROR
                )
            }
        }
    }

    override suspend fun getAll(): Result<List<Counter>> {
        return withContext(Dispatchers.IO) {
            try {
                val counters = counterApi.getCounters().map { dto ->
                    Counter(
                        count = dto.count,
                        id = dto.id,
                        name = dto.title
                    )
                }
                Success(
                    data = counters
                )
            } catch (e: Exception) {
                Result.Failure(
                    error = CounterError.NETWORK_ERROR
                )
            }
        }
    }

    override suspend fun increaseCounter(counter: Counter): Result<List<Counter>> {
        return withContext(Dispatchers.IO) {
            try {
                val counters =
                    counterApi.incrementCounter(counter.toCounterDTO()).map { it.toCounter() }
                Success(
                    data = counters
                )
            } catch (e: Exception) {
                Result.Failure(
                    error = CounterError.NETWORK_ERROR
                )
            }
        }
    }
}