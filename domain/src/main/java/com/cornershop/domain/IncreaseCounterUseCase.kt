package com.cornershop.domain

import com.cornershop.data.models.Counter
import com.cornershop.data.models.Result
import com.cornershop.data.repositories.ICounterRepository

/**
 * Interface that will hold the contract for the use case to increase a counter by any given amount
 */
interface IIncreaseCounterUseCase {

    /**
     * Invocation of the use case
     * @param counterToIncrement [Counter] that the user wants to increment
     * @return [Result] as [Result.Success] with [Boolean] or as [Result.Failure] with the failure
     * reason
     */
    suspend operator fun invoke(counterToIncrement: Counter): Result<Boolean>
}

/**
 * Implementation of [IIncreaseCounterUseCase]
 */
class IncreaseCounterUseCase(
        private val counterRepository: ICounterRepository
): IIncreaseCounterUseCase {

    /**
     * -------------------------------------- PUBLIC METHODS ---------------------------------------
     */

    override suspend operator fun invoke(counterToIncrement: Counter): Result<Boolean> {
        return counterRepository.increaseCounter(counterToIncrement)
    }
}