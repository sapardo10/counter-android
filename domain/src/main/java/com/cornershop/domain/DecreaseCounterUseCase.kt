package com.cornershop.domain

import com.cornershop.data.models.Counter
import com.cornershop.data.models.Result
import com.cornershop.data.repositories.CounterRepository
import com.cornershop.data.repositories.ICounterRepository
import javax.inject.Inject

/**
 * Interface that will hold the contract for the use case to decrease a counter by any given amount
 */
interface IDecreaseCounterUseCase {

    /**
     * Invocation of the use case
     * @param counterToDecrement [Counter] that the user wants to decrement
     * @return [Result] as [Result.Success] with [Boolean] or as [Result.Failure] with the failure
     * reason
     */
    suspend operator fun invoke(counterToDecrement: Counter): Result<Boolean>
}

/**
 * Implementation of [IDecreaseCounterUseCase]
 */
class DecreaseCounterUseCase(
        private val counterRepository: ICounterRepository
): IDecreaseCounterUseCase {

    /**
     * -------------------------------------- PUBLIC METHODS ---------------------------------------
     */

    override suspend operator fun invoke(counterToDecrement: Counter): Result<Boolean> {
        return counterRepository.decreaseCounter(counterToDecrement)
    }
}