package com.cornershop.domain

import com.cornershop.data.models.Counter
import com.cornershop.data.models.Result
import com.cornershop.data.repositories.ICounterRepository
import kotlinx.coroutines.flow.Flow

/**
 * Interface that will hold the contract for the use case to get all the counters
 */
interface IGetAllCountersUseCase {

    /**
     * Invocation of the use case
     * @return A [Flow] with a [List] of [Counter] if success, and a failure reason if failed.
     */
    suspend operator fun invoke(): Flow<Result<List<Counter>>>
}

/**
 * Implementation of [IGetAllCountersUseCase]
 */
class GetAllCountersUseCase(
    private val counterRepository: ICounterRepository
) : IGetAllCountersUseCase {
    override suspend operator fun invoke(): Flow<Result<List<Counter>>> {
        return counterRepository.getAll()
    }
}