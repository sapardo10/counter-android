package com.cornershop.data.repositories

import com.cornershop.data.datasources.ICounterLocalDataSource
import com.cornershop.data.datasources.ICounterRemoteDataSource
import com.cornershop.data.models.Counter
import com.cornershop.data.models.Result
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
        TODO("Not yet implemented")
    }

    override suspend fun decreaseCounter(counterId: Int): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCounter(counterId: Int): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(): Result<List<Counter>> {
        println("repository")
        localDataSource.getAll()
        return Result.Success(listOf())
    }

    ///TODO: Delete this testing method
    fun test() {
        println("repository")
        localDataSource.getAll()
    }

    override suspend fun increaseCounter(counterId: Int): Result<Boolean> {
        TODO("Not yet implemented")
    }
}