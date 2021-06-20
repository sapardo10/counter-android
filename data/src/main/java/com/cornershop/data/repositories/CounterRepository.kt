package com.cornershop.data.repositories

import com.cornershop.data.datasources.ICounterLocalDataSource
import com.cornershop.data.datasources.ICounterRemoteDataSource
import com.cornershop.data.models.Counter
import com.cornershop.data.models.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

/**
 * Implementation of [ICounterRepository]
 */
class CounterRepository @Inject constructor(
    private val localDataSource: ICounterLocalDataSource,
    private val remoteDataSource: ICounterRemoteDataSource
): ICounterRepository {

    /**
     * -------------------------------------- PUBLIC METHODS ---------------------------------------
     */

    override suspend fun createCounter(): Result<Boolean> {
        localDataSource.setAll(listOf(
            Counter(1,1,"holi"),
            Counter(2,2,"chau"),
        ))
        return Result.Success(data = true)
    }

    override suspend fun decreaseCounter(counterId: Int): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCounter(counterId: Int): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(): Flow<Result<List<Counter>>> {
        return when(val remoteResponse = remoteDataSource.getAll()) {
            is Result.Success<List<Counter>> ->  {
                localDataSource.setAll(remoteResponse.data)
                return localDataSource.getAll().map { list -> Result.Success(data = list) }
            }
            is Result.Failure<*> -> {
                flow<Result<List<Counter>>> {
                    emit(remoteResponse)
                }
            }
        }
    }

    override suspend fun increaseCounter(counterId: Int): Result<Boolean> {
        TODO("Not yet implemented")
    }
}