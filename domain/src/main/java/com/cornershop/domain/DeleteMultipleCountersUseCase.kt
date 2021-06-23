package com.cornershop.domain

import com.cornershop.data.models.Counter
import com.cornershop.data.models.Result
import com.cornershop.data.repositories.ICounterRepository

/**
 * Interface that will hold the contract for the use case to delete multiple counters
 */
interface IDeleteMultipleCounterUseCase {

    /**
     * Invocation of the use case
     * @param countersToBeDeleted [List] of [Counter] that the user wants to delete
     * @return [Result] as [Result.Success] with [List] of [Counter] not deleted or as
     * [Result.Failure] with the failure reason
     */
    suspend operator fun invoke(countersToBeDeleted: List<Counter>): List<Result<Counter>>
}

/**
 * Implementation of [IDeleteMultipleCounterUseCase]
 */
class DeleteMultipleCounterUseCase(
    private val counterRepository: ICounterRepository
) : IDeleteMultipleCounterUseCase {

    override suspend operator fun invoke(countersToBeDeleted: List<Counter>): List<Result<Counter>> {
        val results = mutableListOf<Result<Counter>>()
        for (counter in countersToBeDeleted) {
            val booleanResult = counterRepository.deleteCounter(counter)
            if (booleanResult !is Result.Success) {
                results.add(Result.Success(data = counter))
            }
        }
        return results
    }
}