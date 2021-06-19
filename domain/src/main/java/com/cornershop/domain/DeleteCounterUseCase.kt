package com.cornershop.domain

import com.cornershop.data.models.Counter
import com.cornershop.data.models.Result
import com.cornershop.data.repositories.ICounterRepository

/**
 * Interface that will hold the contract for the use case to delete a counter
 */
interface IDeleteCounterUseCase {

    /**
     * Invocation of the use case
     * @param counterToBeDeleted [Counter] that the user wants to delete
     * @return [Result] as [Result.Success] with [Boolean] or as [Result.Failure] with the failure
     * reason
     */
    suspend operator fun invoke(counterToBeDeleted: Counter): Result<Boolean>
}

/**
 * Implementation of [IDeleteCounterUseCase]
 */
class DeleteCounterUseCase(
        private val counterRepository: ICounterRepository
): IDeleteCounterUseCase {
    override suspend operator fun invoke(counterToBeDeleted: Counter): Result<Boolean> {
        TODO("Not yet implemented")
    }
}