package com.cornershop.data.repositories

import com.cornershop.data.datasources.ICounterLocalDataSource
import com.cornershop.data.datasources.ICounterRemoteDataSource
import com.cornershop.data.models.Counter
import com.cornershop.data.models.Result
import com.cornershop.data.models.SuggestionsCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import javax.inject.Inject

/**
 * Implementation of [ICounterRepository]
 */
class CounterRepository @Inject constructor(
    private val localDataSource: ICounterLocalDataSource,
    private val remoteDataSource: ICounterRemoteDataSource
) : ICounterRepository {

    /**
     * -------------------------------------- PUBLIC METHODS ---------------------------------------
     */

    override suspend fun createCounter(title: String): Result<Boolean> {
        return when (val result = remoteDataSource.createCounter(title)) {
            is Result.Success -> {
                localDataSource.setAllCounters(result.data)
                Result.Success(data = true)
            }
            is Result.Failure -> {
                Result.Failure(result.error)

            }
        }
    }

    override suspend fun decreaseCounter(counter: Counter): Result<Boolean> {
        localDataSource.decreaseCounter(counter)
        return when (val result = remoteDataSource.decreaseCounter(counter)) {
            is Result.Success -> {
                localDataSource.setAllCounters(result.data)
                Result.Success(data = true)
            }
            is Result.Failure -> {
                //Rollback to the decrease
                localDataSource.increaseCounter(counter)
                Result.Failure(result.error)

            }
        }
    }

    override suspend fun deleteCounter(counter: Counter): Result<Boolean> {
        return when (val result = remoteDataSource.deleteCounter(counter)) {
            is Result.Success -> {
                localDataSource.deleteCounter(counter)
                Result.Success(data = true)
            }
            is Result.Failure -> {
                Result.Failure(result.error)
            }
        }
    }

    override suspend fun getAll(): Flow<Result<List<Counter>>> {
        return when (val remoteResponse = remoteDataSource.getAll()) {
            is Result.Success<List<Counter>> -> {
                localDataSource.setAllCounters(remoteResponse.data)
                return localDataSource.getAllCounters().map { list -> Result.Success(data = list) }
            }
            is Result.Failure<*> -> {
                merge(
                    flow<Result<List<Counter>>> {
                        emit(remoteResponse)
                    },
                    localDataSource.getAllCounters().map { list -> Result.Success(data = list) }
                )
            }
        }
    }

    override fun getAllCategorySuggestions(): List<SuggestionsCategory> {
        return localDataSource.getAllCounterCategorySuggestions()
    }

    override suspend fun increaseCounter(counter: Counter): Result<Boolean> {
        localDataSource.increaseCounter(counter)
        return when (val result = remoteDataSource.increaseCounter(counter)) {
            is Result.Success -> {
                localDataSource.setAllCounters(result.data)
                Result.Success(data = true)
            }
            is Result.Failure -> {
                //Rollback to the increase
                localDataSource.decreaseCounter(counter)
                Result.Failure(result.error)
            }
        }
    }
}