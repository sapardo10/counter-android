package com.cornershop.counterstest.datasources

import com.cornershop.counterstest.network.apis.CounterApi
import com.cornershop.data.datasources.ICounterRemoteDataSource
import com.cornershop.data.models.Counter
import com.cornershop.data.models.CounterError
import com.cornershop.data.models.Result
import com.cornershop.data.models.Result.Success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class CounterRemoteDataSource constructor(
    private val counterApi: CounterApi
): ICounterRemoteDataSource {

    override suspend fun getAll(): Result<List<Counter>> {
        return withContext(Dispatchers.IO) {
            try {
                val counters = counterApi.getCounters().map { dto -> Counter(
                    count = dto.count,
                    id = dto.id,
                    name = dto.title
                ) }
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